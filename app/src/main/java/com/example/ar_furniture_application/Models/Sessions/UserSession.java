package com.example.ar_furniture_application.Models.Sessions;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ar_furniture_application.Models.User;

public class UserSession {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_CART_ID = "cart_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_ADDRESS = "user_address";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_REGDATE = "user_regdate";
    private static final String KEY_USER_NAME = "user_name";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(User user) {
        editor.putString(KEY_USER_ID, user.getUserID());
        editor.putString(KEY_CART_ID, user.getCartID());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_ADDRESS, user.getAddress());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putString(KEY_USER_REGDATE, user.getRegistrationDate());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_ROLE, "Customer");
        editor.apply();
    }

    public User getCurrentUser() {

        String userId = sharedPreferences.getString(KEY_USER_ID, null);
        String cartId = sharedPreferences.getString(KEY_CART_ID, null);
        String userEmail = sharedPreferences.getString(KEY_USER_EMAIL, null);
        String address = sharedPreferences.getString(KEY_USER_ADDRESS, null);
        String phone = sharedPreferences.getString(KEY_USER_PHONE, null);
        String regdate = sharedPreferences.getString(KEY_USER_REGDATE, null);
        String name = sharedPreferences.getString(KEY_USER_NAME, null);
        String role = sharedPreferences.getString(KEY_USER_ROLE, null);
        if (userEmail != null && role != null) {
            User user = new User(userId,cartId,name,userEmail, phone, address, regdate, role);
            return user;
        }
        return null;
    }

    public class CurrentSessionUser{
        private String userId;
        private String cartId;
        private String email;
        private String address;
        private String phone;
        private String regdate;
        private String name;
        private String role;

        public String getUserId() {
            return userId;
        }

        public String getCartId() {
            return cartId;
        }

        public String getEmail() {
            return email;
        }

        public String getAddress() {
            return address;
        }

        public String getPhone() {
            return phone;
        }

        public String getRegdate() {
            return regdate;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public CurrentSessionUser(String userId, String cartId, String email, String address, String phone, String regdate, String name, String role) {
            this.userId = userId;
            this.cartId = cartId;
            this.email = email;
            this.address = address;
            this.phone = phone;
            this.regdate = regdate;
            this.name = name;
            this.role = role;

        }
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

}

