package com.maxchehab.bulldogbucks;

/**
 * Created by maxchehab on 8/28/17.
 */

public interface OnUserDataListener {
    void onFailure(String error);
    void onSuccess(UserData userData);
}
