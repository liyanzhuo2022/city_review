package com.hmdp.service.impl;

import com.hmdp.repository.UserInfoRepository;
import com.hmdp.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserInfo 服务实现类
 *
 * 只保留骨架，后续可实现业务逻辑
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    // 未来在这里实现业务逻辑
}
