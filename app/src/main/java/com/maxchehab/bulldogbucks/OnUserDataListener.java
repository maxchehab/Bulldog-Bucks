package com.maxchehab.bulldogbucks;

import android.content.Context;

/**
 * Created by maxchehab on 8/28/17.
 */

public interface OnUserDataListener {
    void onFailure(String error);
    void onSuccess(UserData userData);
}
