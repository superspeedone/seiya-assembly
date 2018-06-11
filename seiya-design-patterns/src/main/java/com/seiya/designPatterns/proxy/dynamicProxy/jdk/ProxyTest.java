package com.seiya.designPatterns.proxy.dynamicProxy.jdk;

/**
 * jdk dynamicProxy test
 */
public class ProxyTest {

    public static void main(String[] args) {

        Person person = (Person) new Manufactor().newInstance(new Merchant("123"));
        person.sale();

    }

}
