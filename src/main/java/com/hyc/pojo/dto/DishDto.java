package com.hyc.pojo.dto;

import com.hyc.pojo.Dish;
import com.hyc.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors=new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
