package com.example.raveena.myapplication;

import java.util.HashMap;
import java.util.Map;

public class College {
    private String cname;
     private String address;

    public College() {

    }

    public College(String cname, String address) {
        this.cname = cname;
        this.address = address;

    }

    public String getCname() {
        return cname;
    }

    public String getAddress() {
        return address;
    }

    public void setCname(String cname){
        this.cname = cname;
    }

    public void setAddress(String address){
        this.address = address;
    }
}
