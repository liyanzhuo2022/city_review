package com.hmdp.web;

import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.repository.BlogRepository;
import com.hmdp.repository.UserRepository;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 保存探店博文
        Blog saved = blogRepository.save(blog);
        // 返回id
        return Result.ok(saved.getId());
    }

    @PutMapping("/like/{id}")
    @Transactional
    public Result likeBlog(@PathVariable("id") Long id) {
        // 点赞 +1（原子更新，等价于 MP 的 setSql("liked = liked + 1")）
        int rows = blogRepository.incrementLikedById(id);
        return rows > 0 ? Result.ok() : Result.fail("Blog not found");
    }

    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // Spring Data 的页码从 0 开始；MP 从 1 开始，这里做个 -1 转换
        int pageIndex = Math.max(0, current - 1);
        Pageable pageable = PageRequest.of(pageIndex, SystemConstants.MAX_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Blog> page = blogRepository.findByUserIdOrderByCreateTimeDesc(user.getId(), pageable);
        List<Blog> records = page.getContent();
        return Result.ok(records);
    }

    @GetMapping("/hot")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        int pageIndex = Math.max(0, current - 1);
        Pageable pageable = PageRequest.of(pageIndex, SystemConstants.MAX_PAGE_SIZE);
        Page<Blog> page = blogRepository.findHot(pageable);
        List<Blog> records = page.getContent();

        // 查询用户信息并回填到 transient 字段（与原代码等效）
        records.forEach(blog -> {
            Long userId = blog.getUserId();
            userRepository.findById(userId).ifPresent(u -> {
                blog.setName(u.getNickName());
                blog.setIcon(u.getIcon());
            });
        });
        return Result.ok(records);
    }
}
