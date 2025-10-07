package com.hmdp.service.impl;

import com.hmdp.repository.UserRepository;
import com.hmdp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * User 服务实现类
 *
 * 只保留骨架，后续可实现业务逻辑
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // 未来在这里实现业务逻辑
}
