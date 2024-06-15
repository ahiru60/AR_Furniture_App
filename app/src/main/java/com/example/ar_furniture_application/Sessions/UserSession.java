package com.example.ar_furniture_application.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ar_furniture_application.Roles.User;

public class UserSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(User user) {
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_ROLE, user.getRole());
        editor.apply();
    }

    public User getUser() {
        String userId = sharedPreferences.getString(KEY_USER_ID, null);
        String role = sharedPreferences.getString(KEY_USER_ROLE, null);
        if (userId != null && role != null) {
            return new User(userId, null, null, Role.valueOf(role));
        }
        return null;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}

