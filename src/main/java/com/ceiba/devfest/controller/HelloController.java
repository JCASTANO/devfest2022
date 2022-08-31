package com.ceiba.devfest.controller;


import com.ceiba.devfest.persistence.PersonEntity;
import com.ceiba.devfest.service.MyService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    public static void main(String[] args) {
        //PersonEntity personEntity = new PersonEntity();
        MyService myService = new MyService();
    }
}
