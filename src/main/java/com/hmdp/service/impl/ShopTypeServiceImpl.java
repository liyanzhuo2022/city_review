package com.hmdp.service.impl;

import com.hmdp.repository.ShopTypeRepository;
import com.hmdp.service.ShopTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ShopType 服务实现类
 *
 * 只保留骨架，后续可实现业务逻辑
 */
@Service
@RequiredArgsConstructor
public class ShopTypeServiceImpl implements ShopTypeService {

    private final ShopTypeRepository shopTypeRepository;

    // 未来在这里实现业务逻辑
}
