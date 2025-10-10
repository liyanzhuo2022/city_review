package com.hmdp.service;

import com.hmdp.entity.Shop;

import java.util.List;

public interface ShopService {
    Shop getById(Long id);
    Long saveShop(Shop shop);
    boolean updateShop(Shop shop);
    List<Shop> queryByType(Integer typeId, Integer current);
    List<Shop> queryByName(String name, Integer current);
}
