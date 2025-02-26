package com.mysite.customer.dto;

import com.mysite.customer.model.CustomerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class CustomerDto {
    private Integer id;
    private String name;
    private String number;
    private final CustomerType type;

    public CustomerDto(CustomerType type) {
        this.type = type;
    }
public CustomerDto(Integer id,String name, String number, CustomerType type) {
        this.id=id;
        this.name =name;
        this.number = number;
        this.type = type;
    }
}
