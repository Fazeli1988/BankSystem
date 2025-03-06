package com.mysite.banking.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.banking.dto.AccountDto;
import com.mysite.banking.facade.AccountFacade;
import com.mysite.banking.facade.CustomerFacade;
import com.mysite.banking.facade.impl.AccountFacadeImpl;
import com.mysite.banking.facade.impl.CustomerFacadeImpl;
import com.mysite.banking.model.AccountType;
import com.mysite.banking.model.CustomerType;
import com.mysite.banking.model.FileType;
import com.mysite.banking.service.exception.*;
import com.mysite.banking.util.ScannerWrapper;
import com.mysite.banking.view.component.AbstractCustomerUI;
import com.mysite.banking.dto.CustomerDto;

import java.util.List;
import java.util.function.Function;

public class ConsoleUI  implements AutoCloseable{
    private final ScannerWrapper scannerWrapper;
    private final CustomerFacade customerFacade;
    private final AccountFacade accountFacade;
    private final ObjectMapper objectMapper;
    public ConsoleUI() {
        scannerWrapper = ScannerWrapper.getInstance();
        customerFacade = CustomerFacadeImpl.getInstance();
        accountFacade= AccountFacadeImpl.getInstance();
        objectMapper= new ObjectMapper();
    }
    private void saveOnExit(){
        customerFacade.saveOnExit();
        accountFacade.saveOnExit();
    }
    public void startMenu(){
        customerFacade.initData();
        accountFacade.initData();
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveOnExit));
        int choice;
        do{
            printMainMenu();
            choice= scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);

            switch (choice){
                case 1:
                    customerMenu();
                    break;
                case 2:
                    accountMenu();
                    break;
                case 0:
                    System.out.println("Exit!");
                    break;
                default:
                    System.out.println("Invalid number");
            }
        }while (choice!=0);
    }

    public void accountMenu(){
        int choice;
        do{
            printAccountMenu();
            choice= scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);
            try {


                switch (choice){
                    case 1:
                        addAccount();
                        break;
                    case 2:
                        printAllAccounts();
                        break;
                    case 3:
                        editAccountById();
                        break;
                    case 4:
                        deleteAccountById();
                        break;
                    case 5:
                        printAllDeletedAccounts();
                        break;
                    case 6:
                        saveAccountData();
                        break;
                    case 7:
                        loadAccountData();
                        break;
                    case 8:
                        addAccountData();
                        break;
                    case 9:
                        searchAndPrintAccountByCustomersByName();
                        break;
                    case 10:
                        deposit();
                        break;
                    case 11:
                        withdraw();
                        break;
                    case 0:
                        System.out.println("Exit!");
                        break;
                    default:
                        System.out.println("Invalid number");
                }
            }catch (AccountNotFindException | FileException | EmptyAccountException | ValidationException ex){
                System.out.println(ex.getMessage());
            }
        }while (choice!=0);
    }

    private void withdraw() throws AccountNotFindException ,ValidationException {
        int accountId=scannerWrapper.getUserInput("Enter the account id: ",Integer::valueOf);
        Double amount=scannerWrapper.getUserInput("Enter the amount: ",Double::valueOf);
        accountFacade.withdraw(accountId,amount);
    }

    private void deposit() throws AccountNotFindException {
        int accountId=scannerWrapper.getUserInput("Enter the account id: ",Integer::valueOf);
        Double amount=scannerWrapper.getUserInput("Enter the amount: ",Double::valueOf);
        accountFacade.deposit(accountId,amount);



    }

    public void customerMenu(){
        int choice;
        do{
            printCustomerMenu();
            choice= scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);
            try {


                switch (choice){
                    case 1:
                        addCustomer();
                        break;
                    case 2:
                        printAllCustomers();
                        break;
                    case 3:
                        searchAndPrintCustomersByName();
                        break;
                    case 4:
                        searchAndPrintCustomersByFamily();
                        break;
                    case 5:
                        editCustomerById();
                        break;
                    case 6:
                        deleteCustomerById();
                        break;
                    case 7:
                        printAllDeletedCustomers();
                        break;
                    case 8:
                        saveData();
                        break;
                    case 9:
                        loadData();
                        break;
                    case 10:
                        addData();
                        break;
                    case 0:
                        System.out.println("Exit!");
                        break;
                    default:
                        System.out.println("Invalid number");
                }
            }catch (CustomerNotFindException | FileException | EmptyCustomerException ex){
                System.out.println(ex.getMessage());
            }
        }while (choice!=0);
    }

    private void addData() throws FileException {
        String name=scannerWrapper.getUserInput("Enter the json file name: ",Function.identity());
        customerFacade.addData(name);
    }
    private void addAccountData() throws FileException {
        String name=scannerWrapper.getUserInput("Enter the json file name: ",Function.identity());
        accountFacade.addData(name);
    }

    private void loadData() throws FileException {
        System.out.println("File type:");
        System.out.println("1. Serialize");
        System.out.println("2. Json");
        int choice=scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);
        try {
            FileType fileType=FileType.fromValue(choice);
            String name=scannerWrapper.getUserInput("Enter the file name: ",Function.identity());
            customerFacade.loadData(name,fileType);

        }catch (InvalidType e){
            System.out.println("Invalid type!");
            loadData();
        }
    }

    private void loadAccountData() throws FileException {
        System.out.println("File type:");
        System.out.println("1. Serialize");
        System.out.println("2. Json");
        int choice=scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);
        try {
            FileType fileType=FileType.fromValue(choice);
            String name=scannerWrapper.getUserInput("Enter the file name: ",Function.identity());
            accountFacade.loadData(name,fileType);

        }catch (InvalidType e){
            System.out.println("Invalid type!");
            loadData();
        }
    }

    private void saveAccountData() throws FileException {
        System.out.println("File type:");
        System.out.println("1. Serialize");
        System.out.println("2. Json");
        int choice=scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);
        try {
            FileType fileType=FileType.fromValue(choice);
            String name=scannerWrapper.getUserInput("Enter the file name: ",Function.identity());
            accountFacade.saveData(name,fileType);
        }catch (InvalidType e){
            System.out.println("Invalid type!");
            saveData();
        }
    }
 private void saveData() throws FileException {
        System.out.println("File type:");
        System.out.println("1. Serialize");
        System.out.println("2. Json");
        int choice=scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);
        try {
            FileType fileType=FileType.fromValue(choice);
            String name=scannerWrapper.getUserInput("Enter the file name: ",Function.identity());
            customerFacade.saveData(name,fileType);
        }catch (InvalidType e){
            System.out.println("Invalid type!");
            saveData();
        }
    }


    private void printMainMenu() {
        System.out.println("Menu:");
        System.out.println("0.Exit");
        System.out.println("1. Customer Manager");
        System.out.println("2. Account Manager");

        System.out.println();
    }
    private void printAccountMenu() {
        System.out.println("Menu:");
        System.out.println("0. Back");
        System.out.println("1. Add Account");
        System.out.println("2. Print All Accounts");
        System.out.println("3. Edit Account by id");
        System.out.println("4. Delete Account by id");
        System.out.println("5. Print All deleted Accounts");
        System.out.println("6. Save data");
        System.out.println("7. Load data");
        System.out.println("8. Add data");
        System.out.println("9. Search and print customer by name");
        System.out.println("10. Deposit");
        System.out.println("11. Withdraw");

        System.out.println();
    }
    private void printCustomerMenu() {
        System.out.println("Menu:");
        System.out.println("0. Back");
        System.out.println("1. Add Customer");
        System.out.println("2. Print Active Customers");
        System.out.println("3. Search and print customer by name");
        System.out.println("4. Search and print customer by family");
        System.out.println("5. Edit customer by id");
        System.out.println("6. Delete customer by id");
        System.out.println("7. Print deleted customers");
        System.out.println("8. Save data");
        System.out.println("9. Load data");
        System.out.println("10. Add data");
        System.out.println();
    }

    @Override
    public void close()  {
        scannerWrapper.close();
    }

    private void addAccount(){
        System.out.println("Account Type:");
        System.out.println("1. EURO");
        System.out.println("2. DOLLAR");
        int choice=scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);

        try {
            AccountType accountType=AccountType.fromValue(choice);
            int number=scannerWrapper.getUserInput("Enter customer id: ",Integer::valueOf);
            AccountDto accountDto=new AccountDto(null,accountType,0.0,number);

            accountFacade.addAccount( accountDto);
        } catch (InvalidType e){
            System.out.println("Invalid account type!");
            addAccount();
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            addAccount();
        }
    }

    private void addCustomer(){
        System.out.println("Customer Type:");
        System.out.println("1. Real");
        System.out.println("2. Legal");
        int choice=scannerWrapper.getUserInput("Enter your choice: ",Integer::valueOf);

        try {
            customerFacade.addCustomer( AbstractCustomerUI
                    .fromCustomerType(CustomerType
                            .fromValue(choice))
                    .generateCustomer());
        } catch (DuplicateCustomerException e) {
            System.out.println("It's not possible to select duplicate name and family.");
            addCustomer();
        }catch (InvalidType e){
            System.out.println("Invalid customer type!");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            addCustomer();
        }
    }

    private void printAllCustomers() throws EmptyCustomerException {
        List<CustomerDto> allCustomers = customerFacade.getActiveCustomers();
        System.out.println("All Customer:");
        for (CustomerDto customer : allCustomers) {
            try {
                System.out.println(objectMapper.writeValueAsString(customer));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print customer id "+ customer.getId());
            }
        }
    }
    private void printAllAccounts() throws EmptyAccountException {
        List<AccountDto> allAccounts = accountFacade.getActiveAccounts();
        System.out.println("All Accounts:");
        for (AccountDto account : allAccounts) {
            try {
                System.out.println(objectMapper.writeValueAsString(account));
            } catch (JsonProcessingException e) {
                System.out.println("Error on print account id "+ account.getId());
            }
        }
    }
    private void printAllDeletedAccounts() throws EmptyAccountException {
        List<AccountDto> allAccounts = accountFacade.getDeletedAccounts();
        System.out.println("All Deleted Accounts:");
        for (AccountDto account : allAccounts) {
            System.out.println(account);
        }
    }
    private void printAllDeletedCustomers() throws EmptyCustomerException {
        List<CustomerDto> allCustomers = customerFacade.getDeletedCustomers();
        System.out.println("All Deleted Customers:");
        for (CustomerDto customer : allCustomers) {
            System.out.println(customer);
        }
    }
    private void searchAndPrintAccountByCustomersByName() {
        String name = scannerWrapper.getUserInput("Enter name:", Function.identity());
        List<AccountDto> accountDtoList = accountFacade.searchAccountByCustomersName(name);
        accountDtoList.forEach(System.out::println);
    }
    private void searchAndPrintCustomersByName() {
        String name = scannerWrapper.getUserInput("Enter name:", Function.identity());
        List<AccountDto> accountDtoList = accountFacade.searchAccountByCustomersName(name);
        accountDtoList.forEach(System.out::println);
    }
    private void searchAndPrintCustomersByFamily() {
        String family = scannerWrapper.getUserInput("Enter family:", Function.identity());
        List<CustomerDto>customers= customerFacade.searchCustomersByFamily(family);
        customers.forEach(System.out::println);
    }
    private void editAccountById() throws AccountNotFindException {
        String id = scannerWrapper.getUserInput("Enter the account id:", Function.identity());
        AccountDto accountDto=accountFacade.getAccountById(Integer.valueOf(id));
        System.out.println(accountDto);
        int number=scannerWrapper.getUserInput("Enter new customer id: ",Integer::valueOf);
        accountDto.setCustomerId(number);

        try {
            accountFacade.updateAccount(accountDto);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            editAccountById();
        }
        System.out.println(accountDto);
    }
    private void editCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter id:", Function.identity());
        CustomerDto customerDto= customerFacade.getCustomerById(Integer.valueOf(id));
        System.out.println(customerDto);

        AbstractCustomerUI
                .fromCustomerType(customerDto.getType())
                .editCustomer(customerDto);
        try {
            customerFacade.updateCustomer(customerDto);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            editCustomerById();
        }
        System.out.println(customerDto);
    }

    private void deleteCustomerById() throws CustomerNotFindException {
        String id = scannerWrapper.getUserInput("Enter Id:", Function.identity());
        customerFacade.deleteCustomersById(Integer.valueOf(id));
    }
 private void deleteAccountById() throws AccountNotFindException {
        String id = scannerWrapper.getUserInput("Enter the account Id:", Function.identity());
        accountFacade.deleteAccountById(Integer.valueOf(id));
    }

}

