package com.mysite.banking.dto;

import com.mysite.banking.model.Amount;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Integer id;
    private AmountDto balance;
    private Integer customerId;
}
