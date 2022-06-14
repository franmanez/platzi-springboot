package com.fundamentosplatzi.springboot.fundamentos.bean;

public class MyBeanWithPropertiesImplement implements MyBeanWithProperties{
    private String name;
    private String surname;

    public MyBeanWithPropertiesImplement(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String function() {
        return this.name + " " + this.surname;
    }
}
