package com.mysite.customer.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.customer.model.Customer;
import com.mysite.customer.model.FileType;
import com.mysite.customer.model.RealCustomer;
import com.mysite.customer.service.CustomerService;
import com.mysite.customer.service.exception.*;
import com.mysite.customer.util.MapperWrapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerServiceImpl implements CustomerService {
    private static final CustomerServiceImpl INSTANCE;
    public static CustomerServiceImpl getInstance(){
        return INSTANCE;
    }
    static {
        INSTANCE =new CustomerServiceImpl();
    }
    private CustomerServiceImpl(){

    }
    private ArrayList<Customer> customers=new ArrayList<>();
    private ObjectMapper objectMapper= MapperWrapper.getInstance();
    @Override
    public void deleteCustomersById(Integer id) throws CustomerNotFindException {
        getCustomerById(id).setDeleted(true);

    }
    @Override
    public List<Customer> searchCustomersByFamily(String family) {
        return customers.stream()
                .filter(customer -> !customer.getDeleted())
                .filter(customer -> customer instanceof RealCustomer)
                .map(customer -> (RealCustomer)customer)
                .filter(realCustomer -> realCustomer.getFamily().equalsIgnoreCase(family))
                .collect(Collectors.toList());
    }
    @Override
    public List<Customer> searchCustomersByName(String name) {
        return customers.stream()
                .filter(customer -> customer.getName().equalsIgnoreCase(name)&& !customer.getDeleted())
                .collect(Collectors.toList());
    }
    @Override
    public Customer getCustomerById(Integer id) throws CustomerNotFindException {
        return customers.stream()
                .filter(customer -> !customer.getDeleted())
                .filter(customer -> customer.getId().equals(id))
                .findFirst().orElseThrow(CustomerNotFindException::new);

    }
    @Override
    public List<Customer> getActiveCustomers() throws EmptyCustomerException {
        List<Customer> list = customers.stream()
                .filter(customer -> !customer.getDeleted()).collect(Collectors.toList());

        if (list.isEmpty()){
            throw new EmptyCustomerException();
        }
        return list;
    }
    @Override
    public List<Customer> getDeletedCustomers() throws EmptyCustomerException {
        List<Customer> list = customers.stream()
                .filter(Customer::getDeleted).toList();
        if(list.isEmpty()){
            throw new EmptyCustomerException();
        }
        return list;
    }
    @Override
    public void addCustomer(Customer customer) throws DuplicateCustomerException, ValidationException {
        List<Customer> list = customers.stream()
                .filter(it -> it.equals(customer))
                .findAny()
                .stream().toList();
        if (!list.isEmpty())
            throw new DuplicateCustomerException();

        customers.add(customer);
    }

    @Override
    public void saveData(String name, FileType type) throws FileException {
        switch (type){
            case FileType.JSON -> saveJason(name);
            case FileType.SERIALIZE -> saveSerialize(name);
        }

    }

    private void saveJason(String name) throws FileException {
        try {
            File file=new File(name+".jsn");
            file.createNewFile();
           objectMapper.writeValue(file,customers);
        } catch (IOException e) {
            throw new FileException();
        }
    }

    private void saveSerialize(String name) throws FileException {
        try {
            File file=new File(name+".crm");
            file.createNewFile();
            try (FileOutputStream fileOutputStream=new FileOutputStream(file);
                 ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream)){
                objectOutputStream.writeObject(customers);
            }

        } catch (IOException e) {
            throw new FileException();
        }
    }

    @Override
    public void loadDate(String name, FileType fileType) throws FileException {
        switch (fileType){
            case FileType.JSON -> loadJason(name);
            case FileType.SERIALIZE -> loadSerialize(name);
        }

    }

    @Override
    public void initData() {
        try {
            loadJason("initData");
        } catch (FileException ignored) {

        }
    }

    @Override
    public void saveOnExit() {
        try {
            saveJason("initData");
        } catch (FileException ignored) {
        }
    }

    @Override
    public void addData(String name) throws FileException {
        try {
            ArrayList<Customer> newCustomers=objectMapper.readValue(new File(name + ".jsn"),
                    new TypeReference<ArrayList<Customer>>() {
                    });
            customers.addAll(newCustomers);
        } catch (IOException e) {
            throw new FileException();
        }
    }

    private void loadJason(String name) throws FileException {
        try {
            customers=objectMapper.readValue(new File(name + ".jsn"),
                    new TypeReference<ArrayList<Customer>>() {
                    });
        } catch (IOException e) {
            throw new FileException();
        }
    }

    private void loadSerialize(String name) throws FileException {
        try {

            try (FileInputStream fileInputStream = new FileInputStream(name+".crm");
                 ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream)){
                customers= (ArrayList<Customer>) objectInputStream.readObject();

            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new FileException();
        }
    }


}

