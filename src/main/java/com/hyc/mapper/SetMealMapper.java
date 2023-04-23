package com.hyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.pojo.SetMeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetMealMapper extends BaseMapper<SetMeal> {/*@Update("update setmeal set status=#{status} where id=#{ids}")
    public Integer status(@Param("status")Integer status,@Param("ids")Long ids);*/

}
