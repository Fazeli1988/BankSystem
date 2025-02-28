package com.mysite.customer.dto;

import com.mysite.customer.model.CustomerType;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class CustomerDto {
    private Integer id;
    private String name;
    private String number;
    private final CustomerType type;


}
