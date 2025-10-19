package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.repository.SeckillVoucherRepository;
import com.hmdp.repository.VoucherOrderRepository;
import com.hmdp.repository.VoucherRepository;
import com.hmdp.service.VoucherOrderService;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * VoucherOrder 服务实现类
 *
 * 只保留骨架，后续可实现业务逻辑
 */
@Service
@RequiredArgsConstructor
public class VoucherOrderServiceImpl implements VoucherOrderService {

    private final VoucherOrderRepository voucherOrderRepository;
    private final SeckillVoucherRepository seckillVoucherRepository;
    private final RedisIdWorker redisIdWorker;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result seckillVoucher(Long voucherId) {
        SeckillVoucher voucher = seckillVoucherRepository.findById(voucherId).orElse(null);
        if (voucher == null) {
            return Result.fail("秒杀券不存在");
        }
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀券尚未开始");
        }
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀券已结束");
        }
        if (voucher.getStock() < 1) {
            return Result.fail("库存不足！");
        }

        Long userId = UserHolder.getUser().getId();
        // 给用户id加悲观锁，实现一人一单
        synchronized (userId.toString().intern()) {
            VoucherOrderService proxy = (VoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }

    }

    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        boolean bought = voucherOrderRepository.existsByUserIdAndVoucherId(userId, voucherId);
        if (bought) {
            return Result.fail("用户已经购买过一次！");
        }

        boolean success = seckillVoucherRepository.decreaseStock(voucherId) > 0;
        if (!success) {
            return Result.fail("库存不足！");
        }

        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);
        voucherOrder.setUserId(userId);
        voucherOrder.setStatus(1);
        voucherOrder.setVoucherId(voucherId);
        voucherOrder.setPayType(1);
        voucherOrderRepository.save(voucherOrder);

        return Result.ok(orderId);
    }



}
