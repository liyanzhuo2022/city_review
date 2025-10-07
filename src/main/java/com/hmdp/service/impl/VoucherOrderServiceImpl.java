package com.hmdp.service.impl;

import com.hmdp.repository.VoucherOrderRepository;
import com.hmdp.service.VoucherOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * VoucherOrder 服务实现类
 *
 * 只保留骨架，后续可实现业务逻辑
 */
@Service
@RequiredArgsConstructor
public class VoucherOrderServiceImpl implements VoucherOrderService {

    private final VoucherOrderRepository voucherOrderRepository;

    // 未来在这里实现业务逻辑
}
