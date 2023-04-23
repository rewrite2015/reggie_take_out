package com.hyc.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.hyc.mapper.AddressBookMapper;
import com.hyc.pojo.AddressBook;
import com.hyc.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {



}
