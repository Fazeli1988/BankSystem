package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;
import com.mysite.banking.dto.RealCustomerDto;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.service.exception.CustomerNotFindException;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.validation.ValidationContext;
import com.mysite.banking.util.NumberValidator;

public class AccountValidationContext extends ValidationContext<AccountDto> {
    private final CustomerFacade customerFacade;

    public AccountValidationContext(){
        this.customerFacade = CustomerFacadeImpl.getInstance();

        //Balance validation
        addValidation(accountDto -> {
            Double balance=accountDto.getBalance();
            if(balance<0){
                throw new ValidationException("balance can not be less than zero.");
            }
        });
        //Customer validation
        addValidation(accountDto -> {
            Integer customerId=accountDto.getCustomerId();
            try {
                customerFacade.getCustomerById(customerId);
            } catch (CustomerNotFindException e) {
                throw new ValidationException("Customer Id is not valid.");
            }

        });

    }
}
