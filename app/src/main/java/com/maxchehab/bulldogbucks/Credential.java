package com.maxchehab.bulldogbucks;

/**
 * Created by maxchehab on 8/28/17.
 */

public class Credential {
    private String userID;
    private String password;

    public Credential(String userID, String password){
        this.userID = userID;
        this.password = password;
    }

    public String getUserId(){
        return userID;
    }

    public String getPassword(){
        return password;
    }
}
