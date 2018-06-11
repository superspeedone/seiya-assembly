package com.seiya.designPatterns.proxy.dynamicProxy.cglib;

/**
 * cglib dynamicProxy test
 */
public class ProxyTest {

    public static void main(String[] args) {

        Merchant merchant = (Merchant) new Manufactor().getInstance(Merchant.class);
        merchant.sale();

    }

}
