package com.hyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.pojo.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
