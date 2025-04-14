package com.mysite.banking.sampleTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    @Test
    public void testAdd(){
        Calculator calculator=new Calculator();
        int result=calculator.add(3,5);
        assertEquals(8,result);
    }
}
