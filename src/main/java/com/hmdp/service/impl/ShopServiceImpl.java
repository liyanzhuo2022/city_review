package com.hmdp.service.impl;

import com.hmdp.entity.Shop;
import com.hmdp.repository.ShopRepository;
import com.hmdp.service.ShopService;
import com.hmdp.utils.SystemConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    @Override
    @Transactional(readOnly = true)
    public Shop getById(Long id) {
        return shopRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Long saveShop(Shop shop) {
        Shop saved = shopRepository.save(shop);
        return saved.getId();
    }

    @Override
    @Transactional
    public boolean updateShop(Shop shop) {
        if (shop.getId() == null) {
            return false;
        }
        shopRepository.save(shop);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shop> queryByType(Integer typeId, Integer current) {
        int pageIndex = Math.max(0, (current == null ? 1 : current) - 1);
        Pageable pageable = PageRequest.of(pageIndex, SystemConstants.DEFAULT_PAGE_SIZE);
        return shopRepository.findByTypeId(typeId, pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shop> queryByName(String name, Integer current) {
        int pageIndex = Math.max(0, (current == null ? 1 : current) - 1);
        Pageable pageable = PageRequest.of(pageIndex, SystemConstants.MAX_PAGE_SIZE);
        if (name == null || name.isBlank()) {
            return shopRepository.findAll(pageable).getContent();
        }
        return shopRepository.findByNameContainingIgnoreCase(name, pageable).getContent();
    }
}
