package com.maxchehab.bulldogbucks;

import android.util.Log;

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
        setLocation(location);
        this.accountType = accountType;
        this.amount = amount;
        this.status = status;
        this.message = message;
        this.type = type;
    }

    public Transaction(String location, double amount, String type){
        setLocation(location);
        this.amount = amount;
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
        Log.d("Transaction", location.substring(0,6));

        if(location.substring(0,6).equals("BbOne ")){
            location = location.substring(6);
        }else if (location.substring(0,3).equals("UD ")){
            location = location.substring(3);
        }

        if(isAllUpper(location)){
            Log.d("transaxtion","ALL UPPER" + location);
           location = uppercase(location);
        }


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

    private static boolean isAllUpper(String s) {
        for(char c : s.toCharArray()) {
            if(Character.isLetter(c) && Character.isLowerCase(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    private static String uppercase(String s){
        s = s.toLowerCase();
        String output = "";
        String[] words = s.split("\\s+");
        for(String word : words){
            output += word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
        }

        return output;

    }
}
