package com.mysite.customer.facade.impl;

import com.mysite.customer.dto.CustomerDto;
import com.mysite.customer.facade.CustomerFacade;
import com.mysite.customer.mapper.CustomerMapper;
import com.mysite.customer.model.Customer;
import com.mysite.customer.model.FileType;
import com.mysite.customer.service.CustomerService;
import com.mysite.customer.service.exception.*;
import com.mysite.customer.service.impl.CustomerServiceImpl;
import com.mysite.customer.service.validation.ValidationContext;

import java.util.List;

public class CustomerFacadeImpl implements CustomerFacade {
    private ValidationContext<CustomerDto> validationContext;
    private final CustomerService customerService;
    private static final CustomerFacadeImpl INSTANCE;
    public static CustomerFacadeImpl getInstance(){
        return INSTANCE;
    }
    static {
        INSTANCE =new CustomerFacadeImpl();
    }

    private CustomerFacadeImpl() {
        this.customerService = CustomerServiceImpl.getInstance();
        this.validationContext=new CustomerValidationContext();
    }

    @Override
    public void deleteCustomersById(Integer id) throws CustomerNotFindException {
        customerService.deleteCustomersById(id);
    }

    @Override
    public List<CustomerDto> searchCustomersByFamily(String family) {
        return CustomerMapper.mapToCustomerDtoList(
                customerService.searchCustomersByFamily(family));
    }

    @Override
    public List<CustomerDto> searchCustomersByName(String name) {
        return CustomerMapper.mapToCustomerDtoList(
                customerService.searchCustomersByName(name));
    }

    @Override
    public CustomerDto getCustomerById(Integer id) throws CustomerNotFindException {
        return CustomerMapper.mapToCustomerDto(customerService.getCustomerById(id));
    }

    @Override
    public List<CustomerDto> getActiveCustomers() throws EmptyCustomerException {
        return CustomerMapper.mapToCustomerDtoList(
                customerService.getActiveCustomers());
    }


    @Override
    public List<CustomerDto> getDeletedCustomers() throws EmptyCustomerException {
        return CustomerMapper.mapToCustomerDtoList(
                customerService.getDeletedCustomers());
    }

    @Override
    public void addCustomer(CustomerDto customer) throws DuplicateCustomerException, ValidationException {
        validationContext.validate(customer);
        customerService.addCustomer(CustomerMapper.mapToCustomer(customer));

    }

    @Override
    public void updateCustomer(CustomerDto customerDto) throws ValidationException, CustomerNotFindException {
        validationContext.validate(customerDto);
        Customer customer= customerService.getCustomerById(customerDto.getId());

       CustomerMapper.mapToCustomer(customerDto,customer);

    }

    @Override
    public void saveDate(String name, FileType type) throws FileException {
        customerService.saveData(name,type);
    }

    @Override
    public void loadData(String name, FileType fileType) throws FileException {
        customerService.loadDate(name,fileType);
    }

    @Override
    public void initData() {
        customerService.initData();
    }

    @Override
    public void saveOnExit() {
        customerService.saveOnExit();
    }

    @Override
    public void addData(String name) throws FileException {
        customerService.addData(name);
    }
}
