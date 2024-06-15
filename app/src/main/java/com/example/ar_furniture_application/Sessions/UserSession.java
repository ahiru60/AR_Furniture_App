package com.example.ar_furniture_application.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ar_furniture_application.Roles.Role;
import com.example.ar_furniture_application.Roles.Customer;

public class UserSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Role user;

    public UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(Role user) {
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.apply();
    }

    public String[] getUser() {

        String userId = sharedPreferences.getString(KEY_USER_ID, null);
        String role = sharedPreferences.getString(KEY_USER_ROLE, null);
        if (userId != null && role != null) {
            String [] user = {userId,role};
            return user;
        }
        return null;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

}

