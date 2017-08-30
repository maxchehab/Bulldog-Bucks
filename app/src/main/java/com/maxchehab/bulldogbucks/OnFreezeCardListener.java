package com.maxchehab.bulldogbucks;

/**
 * Created by maxchehab on 8/29/17.
 */

public interface OnFreezeCardListener {
    void onFailure(String error);
    void onSuccess();
}
