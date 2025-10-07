package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.Voucher;
import com.hmdp.repository.VoucherRepository;
import com.hmdp.repository.SeckillVoucherRepository;
import com.hmdp.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Voucher 服务实现类（JPA 风格）
 */
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final SeckillVoucherRepository seckillVoucherRepository;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 这里不再用 Mapper，自定义查询方法交给 Repository
        List<Voucher> vouchers = voucherRepository.findByShopId(shopId);
        return Result.ok(vouchers);
    }

    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        voucherRepository.save(voucher);

        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());

        seckillVoucherRepository.save(seckillVoucher);
    }
}

