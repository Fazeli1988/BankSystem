package com.mysite.customer.dto;

import com.mysite.customer.model.CustomerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class RealCustomerDto extends CustomerDto{
    private String family;

    public RealCustomerDto(Integer id,String name, String number) {
        super(id,name, number, CustomerType.REAL);
    }
  public RealCustomerDto() {
        super(CustomerType.REAL);
    }

}
