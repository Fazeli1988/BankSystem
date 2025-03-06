package com.mysite.banking.facade;

import com.mysite.banking.service.exception.*;
import com.mysite.banking.dto.CustomerDto;
import com.mysite.banking.model.FileType;

import java.util.List;

public interface CustomerFacade {
    void deleteCustomersById(Integer id) throws CustomerNotFindException;
    List<CustomerDto> searchCustomersByFamily(String family);
    List<CustomerDto> searchCustomersByName(String name);
    CustomerDto getCustomerById(Integer id) throws CustomerNotFindException;
    List<CustomerDto> getActiveCustomers() throws EmptyCustomerException;
    List<CustomerDto> getDeletedCustomers() throws EmptyCustomerException;
    void addCustomer(CustomerDto customer) throws DuplicateCustomerException, ValidationException;
    void updateCustomer(CustomerDto customer) throws ValidationException, CustomerNotFindException;

    void saveData(String name, FileType type) throws FileException;

    void loadData(String name, FileType fileType) throws FileException;

    void initData();

    void saveOnExit();

    void addData(String name) throws FileException;
}
