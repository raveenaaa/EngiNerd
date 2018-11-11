package com.example.raveena.myapplication;

import java.util.HashMap;
import java.util.Map;

public class Student {

    private String sname;
    private String cname;
    private String email;
    private String branch;
    private String gender;
    private String bday;
    private int day;
    private int month;
    private int year;

    public Student() {

    }

    public Student(String studName, String collName, String emailId, String branch, String gen, int day, int month, int year) {
        this.sname = studName;
        this.cname = collName;
        this.email = emailId;
        this.branch = branch;
        this.gender = gen;
        this.day = day;
        this.month = month;
        this.year = year;
        this.bday = day+"/"+month+"/"+year;
          }

    public String getSname() {
        return sname;
    }

    public String getCname() {
        return cname;
    }

    public String getEmail() {
        return email;
    }

    public String getBranch() {
        return branch;
    }

    public String getGender() {
        return gender;
    }

    public String getBday(){
        return bday;
    }

    public void setSname() {
        this.sname = sname;
    }

    public void setCname() {
        this.cname = cname;
    }

    public void setEmail() {
        this.email = email;
    }

    public void setBranch() {
        this.branch = email;
    }

    public void setGender() {
        this.gender = gender;
    }

    public void setBday(){
        this.bday = bday;
    }

}
