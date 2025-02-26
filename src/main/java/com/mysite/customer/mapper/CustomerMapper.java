package com.mysite.customer.mapper;

import com.mysite.customer.dto.CustomerDto;
import com.mysite.customer.dto.LegalCustomerDto;
import com.mysite.customer.dto.RealCustomerDto;
import com.mysite.customer.model.Customer;
import com.mysite.customer.model.LegalCustomer;
import com.mysite.customer.model.RealCustomer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerMapper {
    public static List<CustomerDto>mapToCustomerDtoList(List<Customer>customerList){
        return customerList.stream()
                .map(CustomerMapper::mapToCustomerDto)
                .toList();
    }
    private static Field[] getAllFields(Class<?> aClass){
        List<Field>fields=new ArrayList<>();
        while (aClass!=null){
            fields.addAll(Arrays.asList(aClass.getDeclaredFields()));
            aClass=aClass.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }
    public static CustomerDto mapToCustomerDto(Customer customer){
        if(customer instanceof RealCustomer){
            return map((RealCustomer) customer,RealCustomerDto.class);
        }else {
            return map((LegalCustomer) customer,LegalCustomerDto.class);
        }

    }


    private static Field findMachingField(Field sourceField,Field[] destinationFields) {
        for (Field destinationField : destinationFields) {
            if(destinationField.getName().equals(sourceField.getName())){
                destinationField.getType().isAssignableFrom(sourceField.getType());
                return destinationField;
            }
        }
        return null;
    }
    public static <T, U> U map(T source,Class<U> destinationClass){
        try {
        U destination=destinationClass.getDeclaredConstructor().newInstance();
        Field[] sourceFields=getAllFields(source.getClass());
        Field[] destinationFields=getAllFields(destinationClass);
        for (Field sourceField : sourceFields) {
            Field destinationField=findMachingField(sourceField,destinationFields);
            if (destinationField!=null){
                sourceField.setAccessible(true);
                destinationField.setAccessible(true);
                destinationField.set(destination,sourceField.get(source));
            }
        }
        return destination;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException ignored) {
            return null;
        }
    }
    public static Customer mapToCustomer(CustomerDto customerDto,Customer customer){
        if(customerDto instanceof RealCustomerDto){
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    (RealCustomer) customer);
        }else {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    (LegalCustomer) customer);
        }
    }
    public static Customer mapToCustomer(CustomerDto customerDto){
        if(customerDto instanceof RealCustomerDto){
            return mapToRealCustomer((RealCustomerDto) customerDto,
                    new RealCustomer(null,null));
        }else {
            return mapToLegalCustomer((LegalCustomerDto) customerDto,
                    new LegalCustomer(null,null));
        }
    }
    public static RealCustomer mapToRealCustomer(RealCustomerDto realCustomerDto,RealCustomer realCustomer){
        realCustomer.setName(realCustomerDto.getName());
        realCustomer.setNumber(realCustomerDto.getNumber());
        realCustomer.setFamily(realCustomerDto.getFamily());
        return realCustomer;
    }
    public static LegalCustomer mapToLegalCustomer(LegalCustomerDto legalCustomerDto,LegalCustomer legalCustomer){
        legalCustomer.setName(legalCustomerDto.getName());
        legalCustomer.setNumber(legalCustomerDto.getNumber());
        legalCustomer.setFax(legalCustomerDto.getFax());
        return legalCustomer;
    }
}
