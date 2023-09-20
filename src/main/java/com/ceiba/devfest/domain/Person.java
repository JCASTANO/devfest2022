package com.ceiba.devfest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Person {

    //@JsonIgnore
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        System.out.println("Hola mundo");
    }
}
