package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.RealCustomerDto;
import com.mysite.banking.util.NumberValidator;
import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;
import com.mysite.banking.service.exception.ValidationException;
import com.mysite.banking.service.validation.ValidationContext;

public class CustomerValidationContext extends ValidationContext<CustomerDto> {
    public CustomerValidationContext(){
        //Name validation
        addValidation(customer -> {
            String name=customer.getName();
            if(name==null||name.trim().isEmpty()){
                throw new ValidationException("Name must not be empty or null.");
            }
        });
        //Number validation
        addValidation(customer -> {
            String number=customer.getNumber();
            if(!NumberValidator.validate(number)){
                throw new ValidationException("Invalid number format.");
            }
        });
        //Fax validation
        addValidation(customer ->  {
            if(customer instanceof LegalCustomerDto){
                String fax=((LegalCustomerDto) customer).getFax();
                if(!NumberValidator.validate(fax)){
                    throw new ValidationException("Invalid fax number format.");
                }
            }
        });
        //Family validation
        addValidation(customer -> {
            if(customer instanceof RealCustomerDto){
                String family=((RealCustomerDto) customer).getFamily();
                if(family==null||
                        family.trim().isEmpty()||
                        !family.equals(family.toLowerCase())){
                    throw new ValidationException("Family must not be empty or null, and should be in lower case.");
                }
            }
        });
    }
}
