package com.maxchehab.bulldogbucks;

/**
 * Created by maxchehab on 8/28/17.
 */

public class UserData {
    private boolean frozen;
    private double balance;

    public UserData(boolean frozen, double balance){
        this.frozen = frozen;
        this.balance = balance;
    }

    public UserData(){}

    public void setFrozen(boolean value){
        frozen = value;
    }

    public void setBalance(double value){
        balance = value;
    }

    public boolean getFrozen(){
        return frozen;
    }

    public double getBalance(){
        return balance;
    }

}
