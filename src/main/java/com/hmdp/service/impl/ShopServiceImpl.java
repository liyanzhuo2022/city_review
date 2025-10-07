package com.hmdp.service.impl;

import com.hmdp.repository.ShopRepository;
import com.hmdp.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Shop 服务实现类
 *
 * 只保留骨架，后续可实现业务逻辑
 */
@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    // 未来在这里实现业务逻辑
}
