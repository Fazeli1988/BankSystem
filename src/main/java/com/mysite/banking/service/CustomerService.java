package com.mysite.banking.service;

import com.mysite.banking.model.Customer;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.model.FileType;

import java.util.List;

public interface CustomerService {
    void deleteCustomersById(Integer id) throws CustomerNotFindException;
    List<Customer> searchCustomersByFamily(String family);
    List<Customer> searchCustomersByName(String name);
    Customer getCustomerById(Integer id) throws CustomerNotFindException;
    List<Customer> getActiveCustomers() throws EmptyCustomerException;
    List<Customer> getDeletedCustomers() throws EmptyCustomerException;
    void addCustomer(Customer customer) throws DuplicateCustomerException;

    void saveData(String name, FileType type) throws FileException;

    void loadDate(String name, FileType fileType) throws FileException;

    void initData();

    void saveOnExit();

    void addData(String name) throws FileException;
}
