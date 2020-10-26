package com.mwongela.regionalsustainabilitynetwork.utils;


import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PrefManager {

    // Shared Preferences
    static SharedPreferences pref;

    // Editor for Shared preferences
    static SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String PREF_NAME = "TibaPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn", IS_ACTIVATED = "isActivated";;

    // Email address
    private static final String KEY_EMAIL = "email";

    // Username
    public static final String KEY_RETAILER_ID = "id_retailer";
    public static final String KEY_RETAILER_NAME = "name";
    public static final String KEY_RETAILER_PHONE = "phoneuser";
    public static final String KEY_RETAILER_EMAIL = "userEmail";
    public static final String KEY_USER_IMAGE = "imageurl";

    public static final String KEY_LAST_LOGIN = "lastLogin";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_ACCESS_TOKEN= "accessToken";
    public static final String KEY_IS_ACTIVATED= "activated";

    public static final String KEY_USER_NEW_EMAIL = "newemailuser";
    public static final String KEY_USER_PASSWORD = "accpwd";
    public static final String KEY_ORDER_PHONE = "orderphone";
    public static final String KEY_MPESA_NUMBER = "mpesa_number";

    public static final String KEY_DURATION = "duration";
    public static final String KEY_DISTANCE = "distance";

    public static final String KEY_MEMBER = "member";
    public static final String KEY_FROM_ACCOUNT = "accountfrom";

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(JSONObject jsonObject) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_MEMBER, jsonObject.toString());
        editor.putString(KEY_LAST_LOGIN, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        // commit changes
        editor.commit();
    }
    public String getCurrentSession() {
        return pref.getString(KEY_MEMBER, "").trim();
    }

    public void storeCreatedAccount(String phone, String password) {
        // Storing user data in pref
        editor.putString(KEY_RETAILER_PHONE, phone);
        editor.putString(KEY_USER_PASSWORD, password);
        editor.putBoolean(KEY_FROM_ACCOUNT, true);

        // commit changes
        editor.commit();
    }
    public void setIsActivated(boolean value) {
        // Storing user data in pref
        editor.putBoolean(IS_ACTIVATED, value);

        // commit changes
        editor.commit();
    }
    public void setKeyFromAccount(boolean value) {
        // Storing user data in pref
        editor.putBoolean(KEY_FROM_ACCOUNT, value);

        // commit changes
        editor.commit();
    }
    public void setImageURL(String imageURL) {
        // Storing user data in pref
        editor.putString(KEY_USER_IMAGE, imageURL);

        // commit changes
        editor.commit();
    }
    public void setOrderPhone(String orderPhone) {
        // Storing user data in pref
        editor.putString(KEY_ORDER_PHONE, orderPhone);
        // commit changes
        editor.commit();
    }
    public static void setRated(String orderID) {
        // Storing user data in pref
        editor.putBoolean(orderID, true);
        // commit changes
        editor.commit();
    }
    public void setMpesaPhone(String mpesaPhone) {
        // Storing user data in pref
        editor.putString(KEY_MPESA_NUMBER, mpesaPhone);
        // commit changes
        editor.commit();
    }
    public void setEmail(String email) {
        // Storing user data in pref
        editor.putString(KEY_RETAILER_EMAIL, email);
        // commit changes
        editor.commit();
    }
    public void setAccessToken(String accessToken) {
        // Storing user data in pref
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        // commit changes
        editor.commit();
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public void saveSharedSetting(String settingName, String settingValue) {
        editor.putString(settingName, settingValue);
        editor.apply();
    }
    public String readSharedSetting(String settingName, String defaultValue) {
        return pref.getString(settingName, defaultValue);
    }
    public String getImageURL() {
        return pref.getString(KEY_USER_IMAGE, null);
    }
    public static boolean getRated(String key) {
        return pref.getBoolean(key, false);
    }
    public String getOrderPhone() {
        return pref.getString(KEY_ORDER_PHONE, null);
    }

    public static String getPhone() {
        return pref.getString(KEY_RETAILER_PHONE, null);
    }

    public static String getLastLogin() {
        return pref.getString(KEY_LAST_LOGIN, null);
    }
    public static String getIsActivated() {
        return pref.getString(KEY_IS_ACTIVATED, null);
    }

    public static String getAccessToken() {
        return pref.getString(KEY_ACCESS_TOKEN, null);
    }

    public static int getUserID() {
        return pref.getInt(KEY_RETAILER_ID, -1);
    }
    public static String getRetailerName() {
        return pref.getString(KEY_RETAILER_NAME, "").trim();
    }

    public static String getLoginPassword() {
        return pref.getString(KEY_USER_PASSWORD, "").trim();
    }
    public static String getEmail() {
        return pref.getString(KEY_RETAILER_EMAIL, "").trim();
    }
    public static String getMpesaNumber() {
        return pref.getString(KEY_MPESA_NUMBER, "").trim();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isActivated() {
        return pref.getBoolean(IS_ACTIVATED, false);
    }
    public boolean isFromAccount() {
        return pref.getBoolean(KEY_FROM_ACCOUNT, false);

    }

    public static void setDistance(String distance) {
        editor.putString(KEY_DISTANCE, distance);
        editor.apply();
    }
    public static void setDuration(String duration) {
        editor.putString(KEY_DURATION, duration);
        editor.apply();
    }

    public static String getDistance() {
        return pref.getString(KEY_DISTANCE, null);
    }
    public static String getDuration() {
        return pref.getString(KEY_DURATION, null);
    }
}

