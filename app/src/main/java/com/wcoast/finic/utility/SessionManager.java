package com.wcoast.finic.utility;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wcoast.finic.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREFER_NAME = "FINIC";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_AUTH = "access_token";
    public static final String KEY_IS_ACTIVE = "is_active";
    public static final String KEY_REFER_CODE = "refer_code";
    public static final String KEY_IS_MOBILE_VERIFIED = "is_mobile_verified";
    public static final String KEY_PROFILE_PIC = "profile_pic";
    public static final String KEY_ADDRESS = "key_address";
    public static final String KEY_TOKEN_TYPE = "token_type";
    public static final String KEY_WALLET_AMOUNT = "wallet_amount";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FCM_TOKEN = "fcm_login";
    public static final String KEY_DEEPLINK_REFERRAL = "deeplink_referral";

    public static final String KEY_LAST_CHAT_USER_ID = "last_chat_user_id";

    public static final String KEY_IS_NOTI_RUNNING = "IsRunning";
    public static final String KEY_IS_CHAT_USER_RUNNING = "IsChatUserRunning";
    public static final String KEY_IS_CHAT_RUNNING = "IsChatRunning";
    public static final String KEY_IS_WALLET_RUNNING = "IsWalletRunning";

    // Email address (make variable public to access from outside)

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create User Login will check whether
     * the user is logged in or not
     */
    public void createUserLoginSession(String user_id, String name, String mobile, String email, String auth, String tokenType, Boolean active, String refer, Boolean mobile_verified, String profile_pic, String address, String wallet_amount) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER_ID, user_id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOBILE_NO, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_AUTH, auth);
        editor.putString(KEY_TOKEN_TYPE, tokenType);
        editor.putBoolean(KEY_IS_ACTIVE, active);
        editor.putString(KEY_REFER_CODE, refer);
        editor.putBoolean(KEY_IS_MOBILE_VERIFIED, mobile_verified);
        editor.putString(KEY_PROFILE_PIC, profile_pic);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_WALLET_AMOUNT, wallet_amount);
        editor.commit();
    }

    public void saveFCMToken(String fcmToken) {
        editor.putString(KEY_FCM_TOKEN, fcmToken);
        editor.commit();
    }

    public void saveImagePath(String imagePath) {
        editor.putString(KEY_PROFILE_PIC, imagePath);
        editor.commit();
    }

    public String getUserId() {
        String userID = pref.getString(KEY_USER_ID, null);
        return userID;
    }

    public void putRunningStatus(Boolean IsChatUserRunning, Boolean IsChatRunning, Boolean IsNotificationRunning, Boolean IsWalletRunning) {
        editor.putBoolean(KEY_IS_NOTI_RUNNING, IsNotificationRunning);
        editor.putBoolean(KEY_IS_CHAT_RUNNING, IsChatRunning);
        editor.putBoolean(KEY_IS_CHAT_USER_RUNNING, IsChatUserRunning);
        editor.putBoolean(KEY_IS_WALLET_RUNNING, IsWalletRunning);
        editor.commit();
    }

    public Map<String, Boolean> getRunningStatus() {
        Map<String, Boolean> running = new HashMap<>();
        running.put(KEY_IS_NOTI_RUNNING, pref.getBoolean(KEY_IS_NOTI_RUNNING, false));
        running.put(KEY_IS_CHAT_USER_RUNNING, pref.getBoolean(KEY_IS_CHAT_USER_RUNNING, false));
        running.put(KEY_IS_CHAT_RUNNING, pref.getBoolean(KEY_IS_CHAT_RUNNING, false));
        return running;
    }

    public void addSubCatIDAndName(String id, String name) {
        editor.putString(Constant.SUB_CAT_ID, id);
        editor.putString(Constant.SUB_CAT_NAME, name);
        editor.commit();
    }

    public void saveTempReferral(String referCode) {
        editor.putString(KEY_DEEPLINK_REFERRAL, referCode);
        editor.commit();
    }

    public void setLastChatUserId(int user_id) {

        editor.putInt(KEY_LAST_CHAT_USER_ID, user_id);
        editor.commit();
    }

    public int getLastChatUserId() {
        return pref.getInt(KEY_LAST_CHAT_USER_ID, 0);
    }


    public String getTempReferral() {
        if (pref.contains(KEY_DEEPLINK_REFERRAL)) {
            return pref.getString(KEY_DEEPLINK_REFERRAL, null);
        } else return "";
    }

    public void saveUpdatedUser(String name, String email, String address) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ADDRESS, address);
        editor.commit();
    }


    public HashMap<String, String> getSubCatIDAndName() {
        HashMap<String, String> data = new HashMap<String, String>();

        // user details
        data.put(Constant.SUB_CAT_ID, pref.getString(Constant.SUB_CAT_ID, null));
        data.put(Constant.SUB_CAT_NAME, pref.getString(Constant.SUB_CAT_NAME, null));

        return data;
    }

    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user details
        user.put(KEY_WALLET_AMOUNT, pref.getString(KEY_WALLET_AMOUNT, null));
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_REFER_CODE, pref.getString(KEY_REFER_CODE, null));
        user.put(KEY_PROFILE_PIC, pref.getString(KEY_PROFILE_PIC, null));
        user.put(KEY_MOBILE_NO, pref.getString(KEY_MOBILE_NO, null));
        user.put(KEY_AUTH, pref.getString(KEY_AUTH, null));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_TOKEN_TYPE, pref.getString(KEY_TOKEN_TYPE, null));

        // return user
        return user;
    }


    public String getFcmToken() {
        return pref.getString(KEY_FCM_TOKEN, null);
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}