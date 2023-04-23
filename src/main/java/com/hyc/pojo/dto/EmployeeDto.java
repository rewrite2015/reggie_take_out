package com.hyc.pojo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class EmployeeDto {
    private List list=new ArrayList<>();
    private Long total;

    public EmployeeDto(List list, Long total) {
        this.list = list;
        this.total = total;
    }

    public EmployeeDto() {
    }
}
