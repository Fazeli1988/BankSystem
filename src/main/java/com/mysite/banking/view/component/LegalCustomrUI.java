package com.mysite.banking.view.component;

import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.dto.LegalCustomerDto;

import java.util.function.Function;

public class LegalCustomrUI extends AbstractCustomerUI{
    public LegalCustomrUI() {
        super();
    }

    @Override
    public CustomerDto additionalGenerateCustomer(String name, String number) {
        String fax = scannerWrapper.getUserInput("Enter fax:", Function.identity());
        LegalCustomerDto legalCustomer=new LegalCustomerDto(null,name,number);
        legalCustomer.setFax(fax);
        return legalCustomer;
    }

    @Override
    public void editCustomer(CustomerDto customer) {
        LegalCustomerDto legalCustomer=(LegalCustomerDto) customer;
        String number = scannerWrapper.getUserInput("Enter number:", Function.identity());
        customer.setNumber(number);
        String fax = scannerWrapper.getUserInput("Enter fax number:", Function.identity());
        legalCustomer.setFax(fax);
    }
}
