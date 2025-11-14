package com.example.nganjukabirupa;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_ID_CUSTOMER = "id_customer";
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveIdCustomer(String id) {
        prefs.edit().putString(KEY_ID_CUSTOMER, id).apply();
    }

    public String getIdCustomer() {
        return prefs.getString(KEY_ID_CUSTOMER, null);
    }

    public boolean isLoggedIn() {
        return getIdCustomer() != null;
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
