package com.hmdp.repository;


import com.hmdp.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    // 基于实体属性名 shopId 的派生查询（注意是实体字段名，不是列名）
    List<Voucher> findByShopId(Long shopId);

    @Query(value = """
        SELECT
            v.id, v.shop_id, v.title, v.sub_title, v.rules, v.pay_value,
            v.actual_value, v.type, v.status,
            sv.stock, sv.begin_time, sv.end_time
        FROM tb_voucher v
        LEFT JOIN tb_seckill_voucher sv ON v.id = sv.voucher_id
        WHERE v.shop_id = :shopId AND v.status = 1
        """, nativeQuery = true)
    List<Object[]> queryVoucherOfShop(@Param("shopId") Long shopId);
}
