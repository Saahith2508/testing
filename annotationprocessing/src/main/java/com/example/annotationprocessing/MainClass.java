package com.example.annotationprocessing;

import com.example.processor.TestClass;
import com.example.processor.TestLogic;
import com.example.processor.TestMethod;


public class MainClass {
    public int add(int a, int b) {
        return a + b;
    }


    public String greet(String name) {
        return "Hello, " + name + "!";
    }



    public void greetTest(){
        String name = "saahith";
        String result = greet(name);
        // Add assertions or other test logic for greet method
        // For example:
        org.junit.Assert.assertEquals("Hello, saahith!", result);
    }

    public static void main(String[] args) {
        GroovyValidationFramework validationFramework = new GroovyValidationFramework();

        boolean isEven = (boolean) validationFramework.validate("validation", "isEven",2);
        if(isEven){
            System.out.println(isEven);
        }

    }
}
