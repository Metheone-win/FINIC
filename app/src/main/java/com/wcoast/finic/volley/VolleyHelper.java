package com.wcoast.finic.volley;

import android.util.Log;

import com.wcoast.finic.model.ModelBankDetails;
import com.wcoast.finic.utility.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by deepak on 07/03/2018.
 * Helper Class to define constants, urls and json used in volley
 */
public class VolleyHelper {

    private static final String TAG = VolleyHelper.class.getSimpleName();

    public static final int REQ_CODE_SIGN_UP = 1;
    public static final int REQ_CODE_LOGIN = 2;
    public static final int REQ_CODE_ACCOUNT_ACTIVATED = 3;
    public static final int REQ_CODE_UPDATE_PROFILE = 4;
    public static final int REQ_CODE_GET_REFERRALS = 5;
    public static final int REQ_CODE_VERIFY_USER = 6;
    public static final int REQ_CODE_FORGET_PASS = 7;
    public static final int REQ_CODE_CHECKSUM_PAYTM = 8;
    public static final int REQ_CODE_CHANGE_PASSWORD = 9;
    public static final int REQ_CODE_CHANGE_PROFILE_IMAGE = 10;
    public static final int REQ_CODE_CONTACT_US = 11;
    public static final int REQ_CODE_NOTIFICATION = 12;

    public static final int REQ_GET_BANK_DETAIL = 13;
    public static final int REQ_SET_BANK_DETAIL = 14;
    public static final int REQ_REDEEM_POINTS = 15;
    public static final int REQ_LEVEL_DETAILS = 16;
    public static final int REQ_PAYUMONEY_RESPONSE = 17;


    // Live server base url..
    //private static final String BASE_URL = "http://app.wifichai.com/index.php/webservices/";
    // Test server base url..
    private static final String BASE_URL = "http://thoughtwin.com/mlm/public/api/";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String SIGN_UP_URL = BASE_URL + "signup";
    public static final String FORGET_PASSWORD_URL = BASE_URL + "forgot-password";
    public static final String VERIFY_USER_URL = BASE_URL + "verify-mobile";
    public static final String CHANGE_PASSWORD_URL = BASE_URL + "change-password";
    public static final String UPDATE_PROFILE_IMAGE_URL = BASE_URL + "profile-pic-upload";
    public static final String UPDATE_PROFILE_URL = BASE_URL + "user-profile";
    public static final String CONTACT_US_URL = BASE_URL + "contact";
    public static final String CHECKSUM_PAYTM_URL = BASE_URL + "pay-activation";
    public static final String NOTIFICATION_URL = BASE_URL + "notifications";
    public static final String GET_REFERRALS_URL = BASE_URL + "referral-details";
    public static final String ACCOUNT_ACTIVATE_URL = BASE_URL + "payment-response";
    public static final String LEVEL_DETAILS_URL = BASE_URL + "level-details";

    public static final String GET_BANK_DETAIL = BASE_URL + "get-bank-redeem-details";
    public static final String SET_BANK_DETAIL = BASE_URL + "bank-detail";
    public static final String REDEEM_REQUEST = BASE_URL + "redeem-request";


    /**
     * Convert params to json-object for sending
     */
    public JSONObject getLoginParam(String mobile, String password, String fcmToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_MOBILE_NO, mobile);
            jsonObject.put(Constant.PASSWORD, password);
            jsonObject.put(Constant.FCM_TOKEN, fcmToken);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getChangePassParams(String mobile, String otp, String newPassword) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_MOBILE_NO, mobile);
            jsonObject.put(Constant.OTP, otp);
            jsonObject.put(Constant.NEW_PASSWORD, newPassword);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }


    public JSONObject getSignUpParam(String full_name, String mobile, String password, String email, String referCode, String fcmToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.FULL_NAME, full_name);
            jsonObject.put(Constant.USER_MOBILE_NO, mobile);
            jsonObject.put(Constant.USER_EMAIL, email);
            jsonObject.put(Constant.PASSWORD, password);
            jsonObject.put(Constant.REFER_CODE, referCode);
            jsonObject.put(Constant.FCM_TOKEN, fcmToken);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getNotificationParams(String page) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.PAGE, page);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getChecksumParams(String userId, String payAmount) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, userId);
            jsonObject.put(Constant.ACTIVATION_AMOUNT, payAmount);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject payUmoneyResponseParams(boolean status, String userId, String referCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STATUS, status);
            jsonObject.put(Constant.USER_ID, userId);
            jsonObject.put(Constant.REFER_CODE, referCode);
            Log.e(TAG,jsonObject.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getContactUsParams(String full_name, String mobile, String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.FULL_NAME, full_name);
            jsonObject.put(Constant.USER_MOBILE_NO, mobile);
            jsonObject.put(Constant.USER_QUERY, message);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getReferralparams(String userId, int level_no) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, userId);
            jsonObject.put(Constant.LEVEL_NO, level_no);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getUserIdParams(String userId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_ID, userId);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getRedeemPoints(String redeemPoints) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.REQUEST_POINT, redeemPoints);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject setBankDetail(ModelBankDetails bankDetails) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(Constant.BANK_NAME, bankDetails.getBankName());
            jsonObject.put(Constant.BRANCH_NAME, bankDetails.getBranchName());
            jsonObject.put(Constant.ACCOUNT_HOLDER_NAME, bankDetails.getAccountHolderName());
            jsonObject.put(Constant.ACCOUNT_NO, bankDetails.getAccountNo());
            jsonObject.put(Constant.IFSC_CODE, bankDetails.getIfscCode());

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getVerificationParams(String mobile, String otp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_MOBILE_NO, mobile);
            jsonObject.put(Constant.OTP, otp);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getForgetPassParam(String mobile) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.USER_MOBILE_NO, mobile);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getPaytmActivationparams(Boolean status, String userId, String orderId, String txnId, String txnDate, String txnAmount, String referCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.STATUS, status);
            jsonObject.put(Constant.USER_ID, userId);
            jsonObject.put(Constant.TXN_AMOUNT, txnAmount);
            jsonObject.put(Constant.REFER_CODE, referCode);
            jsonObject.put(Constant.ORDER_ID, orderId);
            jsonObject.put(Constant.TXN_DATE, txnDate);
            jsonObject.put(Constant.TXN_ID, txnId);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getUpdateProfileParams(String name, String emailId, String address, String latitude, String longitude) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constant.FULL_NAME, name);
            jsonObject.put(Constant.USER_EMAIL, emailId);
            jsonObject.put(Constant.ADDRESS, address);
            jsonObject.put(Constant.LATITUDE, latitude);
            jsonObject.put(Constant.LONGITUDE, longitude);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonObject;
    }

}
