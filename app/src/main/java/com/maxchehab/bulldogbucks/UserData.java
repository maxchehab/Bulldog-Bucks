package com.maxchehab.bulldogbucks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxchehab on 8/28/17.
 */

public class UserData {
    private boolean frozen;
    private double balance;
    private String swipes;
    private String swipeType;
    private List<Transaction> transactions = new ArrayList<>();

    public UserData(boolean frozen, double balance){
        this.frozen = frozen;
        this.balance = balance;
        this.swipeType = "N/A";
    }

    public UserData(){}

    public void setFrozen(boolean value){
        frozen = value;
    }

    public void setBalance(double value){
        balance = value;
    }

    public void setSwipes(String value){
        swipes = value;
    }

    public void setSwipeType(String value){
        swipeType = value;
    }

    public void addTransactions(Transaction transaction){
        this.transactions.add(transaction);
    }

    public boolean getFrozen(){
        return frozen;
    }

    public double getBalance(){
        return balance;
    }

    public String getSwipes(){
        return swipes;
    }

    public String getSwipeType(){
        return swipeType;
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

}
