package com.mysite.banking.facade;

import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.exception.*;

import java.util.List;

public interface AccountFacade {
    void deleteAccountById(Integer id) throws AccountNotFindException;
    List<AccountDto> getActiveAccounts() throws EmptyAccountException;
    List<AccountDto> getDeletedAccounts() throws EmptyAccountException;
    AccountDto getAccountById(Integer id) throws AccountNotFindException;
    void addAccount(AccountDto accountDto) throws ValidationException;
    void updateAccount(AccountDto accountDto) throws ValidationException, AccountNotFindException;

    void saveData(String name, FileType type) throws FileException;

    void loadData(String name, FileType fileType) throws FileException;

    void initData();

    void saveOnExit();

    void addData(String name) throws FileException;

    List<AccountDto> searchAccountByCustomersName(String name);

    void deposit(int accountId, Double amount) throws AccountNotFindException ;

    void withdraw(int accountId, Double amount)throws AccountNotFindException ,ValidationException;
}
