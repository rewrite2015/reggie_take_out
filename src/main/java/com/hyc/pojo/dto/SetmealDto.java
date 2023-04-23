package com.hyc.pojo.dto;

import com.hyc.pojo.SetMeal;
import com.hyc.pojo.SetMealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends SetMeal {

    private List<SetMealDish> setmealDishes;

    private String categoryName;
}
