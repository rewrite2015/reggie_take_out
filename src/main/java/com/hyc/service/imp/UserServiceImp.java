package com.hyc.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.mapper.UserMapper;
import com.hyc.pojo.User;
import com.hyc.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {
}
