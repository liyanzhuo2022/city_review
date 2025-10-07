package com.hmdp.web;


import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.repository.ShopTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shop-type")
@RequiredArgsConstructor
public class ShopTypeController {

    private final ShopTypeRepository shopTypeRepository;

    @GetMapping("/list")
    public Result queryTypeList() {
        // 按 sort 字段升序查询所有店铺类型
        List<ShopType> typeList = shopTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "sort"));
        return Result.ok(typeList);
    }
}
