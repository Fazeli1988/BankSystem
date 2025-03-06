package com.mysite.banking.facade.impl;

import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.mapper.AccountMapstruct;
import com.mysite.banking.model.Account;
import com.mysite.banking.model.Customer;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.AccountService;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.service.impl.AccountServiceImpl;
import com.mysite.banking.service.impl.CustomerServiceImpl;
import com.mysite.banking.service.validation.ValidationContext;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

public class AccountFacadeImpl implements AccountFacade {
    private ValidationContext<AccountDto> validationContext;
    private final AccountService accountService;
    private final CustomerService customerService;
    private final AccountMapstruct accountMapstruct;
    private static final AccountFacadeImpl INSTANCE;
    public static AccountFacadeImpl getInstance(){
        return INSTANCE;
    }
    static {
        INSTANCE =new AccountFacadeImpl();
    }

    private AccountFacadeImpl() {
        this.accountMapstruct=Mappers.getMapper(AccountMapstruct.class);
        this.accountService = AccountServiceImpl.getInstance();
        this.validationContext=new AccountValidationContext();
        this.customerService=CustomerServiceImpl.getInstance();
    }

    @Override
    public void deleteAccountById(Integer id) throws AccountNotFindException {
        accountService.deleteAccountById(id);
    }
    @Override
    public List<AccountDto> getActiveAccounts() throws EmptyAccountException {
        return accountMapstruct.mapToAccountDtoList(
                accountService.getActiveAccounts());
    }
    @Override
    public List<AccountDto> getDeletedAccounts() throws EmptyAccountException {
        return accountMapstruct.mapToAccountDtoList(
                accountService.getDeletedAccounts());
    }
    @Override
    public AccountDto getAccountById(Integer id) throws AccountNotFindException {
        return accountMapstruct.mapToAccountDto(accountService.getAccountById(id));
    }
    @Override
    public void addAccount(AccountDto accountDto) throws  ValidationException {
        validationContext.validate(accountDto);
        accountService.addAccount(accountMapstruct.mapToAccount(accountDto));

    }
    @Override
    public void updateAccount(AccountDto accountDto) throws ValidationException, AccountNotFindException {
        validationContext.validate(accountDto);
        Account account= accountService.getAccountById(accountDto.getId());

        accountMapstruct.mapToAccount(accountDto,account);

    }
    @Override
    public void saveData(String name, FileType type) throws FileException {
        accountService.saveData(name,type);
    }

    @Override
    public void loadData(String name, FileType fileType) throws FileException {
        accountService.loadDate(name,fileType);
    }

    @Override
    public void initData() {
        accountService.initData();
    }

    @Override
    public void saveOnExit() {
        accountService.saveOnExit();
    }

    @Override
    public void addData(String name) throws FileException {
        accountService.addData(name);
    }

    @Override
    public List<AccountDto> searchAccountByCustomersName(String name) {
        List<Customer> customers = customerService.searchCustomersByName(name);
        List<Account> accounts=new ArrayList<>();
        for (Customer customer : customers) {
            accounts.addAll(accountService.getAccountByCustomerId(customer.getId()))  ;
        }
        return accountMapstruct.mapToAccountDtoList(accounts);
    }

    @Override
    public void deposit(int accountId, Double amount) throws AccountNotFindException {
        accountService.deposit(accountId,amount);
    }

    @Override
    public void withdraw(int accountId, Double amount) throws AccountNotFindException, ValidationException {
        accountService.withdraw(accountId,amount);

    }
}
