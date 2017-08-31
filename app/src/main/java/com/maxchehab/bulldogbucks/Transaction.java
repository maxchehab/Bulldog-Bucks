package com.maxchehab.bulldogbucks;

import java.util.Date;

/**
 * Created by maxchehab on 8/30/17.
 */

public class Transaction {
    private Date date;
    private String location;
    private String accountType;
    private double amount;
    private String status;
    private String message;
    private String type;

    public Transaction(Date date, String location, String accountType, double amount, String status, String message, String type){
        this.date = date;
        this.location = location;
        this.accountType = accountType;
        this.amount = amount;
        this.status = status;
        this.message = message;
        this.type = type;
    }


    public Date getDate(){
        return date;
    }

    public String getLocation(){
        return location;
    }

    public String getAccountType(){
        return accountType;
    }

    public double getAmount(){
        return amount;
    }

    public String getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public String getType(){
        return type;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setAccountType(String accountType){
        this.accountType = accountType;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setType(String type){
        this.type = type;
    }
}
