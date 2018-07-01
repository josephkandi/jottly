package com.peruzal.jottly.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by joseph on 7/1/18.
 */

public  class SharedPreferencesUtils {
    public static String getEmailAddress(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getString(Constants.EMAIL_ADDRESS, null);
    }
}
