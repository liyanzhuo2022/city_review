package com.hmdp.web;


import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.repository.ShopRepository;
import com.hmdp.utils.SystemConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    public final ShopRepository shopRepository;

    /**
     * 根据id查询商铺信息
     * @param id 商铺id
     * @return 商铺详情数据
     */
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return Result.ok(shopRepository.findById(id).orElse(null));
    }

    /**
     * 新增商铺信息
     * @param shop 商铺数据
     * @return 商铺id
     */
    @PostMapping
    public Result saveShop(@RequestBody Shop shop) {
        // 写入数据库
        shopRepository.save(shop);
        // 返回店铺id
        return Result.ok(shop.getId());
    }

    /**
     * 更新商铺信息
     * @param shop 商铺数据
     * @return 无
     */
    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {
        if (shop.getId() == null) {
            return Result.fail("id is required");
        }
        shopRepository.save(shop);
        return Result.ok();
    }

    /**
     * 根据商铺类型分页查询商铺信息
     * @param typeId 商铺类型
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        int pageIndex = Math.max(0, current - 1);
        Pageable pageable = PageRequest.of(pageIndex, SystemConstants.DEFAULT_PAGE_SIZE);
        List<Shop> records = shopRepository.findByTypeId(typeId, pageable).getContent();
        return Result.ok(records);
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     * @param name 商铺名称关键字
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        int pageIndex = Math.max(0, current - 1);
        Pageable pageable = PageRequest.of(pageIndex, SystemConstants.MAX_PAGE_SIZE);

        List<Shop> records;
        if (name == null || name.isBlank()) {
            records = shopRepository.findAll(pageable).getContent();
        } else {
            records = shopRepository.findByNameContainingIgnoreCase(name, pageable).getContent();
        }
        return Result.ok(records);
    }
}
