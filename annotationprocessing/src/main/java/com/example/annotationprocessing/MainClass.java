package com.example.annotationprocessing;

import com.example.processor.TestClass;
import com.example.processor.TestLogic;
import com.example.processor.TestMethod;

@TestClass
public class MainClass {
    public int add(int a, int b) {
        return a + b;
    }

    @TestMethod
    public String greet(String name) {
        return "Hello, " + name + "!";
    }


    @TestLogic(forMethod = "greet")
    public void greetTest(){
        String name = "saahith";
        String result = greet(name);
        // Add assertions or other test logic for greet method
        // For example:
        org.junit.Assert.assertEquals("Hello, saahith!", result);
    }
}
