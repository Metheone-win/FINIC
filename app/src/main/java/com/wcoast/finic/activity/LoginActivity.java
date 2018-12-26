package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;
import com.wcoast.finic.utility.Validation;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements VolleyResponseListener, View.OnClickListener {

    @BindView(R.id.til_mobile_no)
    TextInputLayout etMobile;
    @BindView(R.id.til_password)
    TextInputLayout etPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_forget_pass)
    Button btnForgetPass;
    @BindView(R.id.btn_SignUp)
    Button btnCreate;
    private String TAG = LoginActivity.class.getSimpleName();
    private String fcmToken;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (new SessionManager(this).isUserLoggedIn()) {

            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);

        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                fcmToken = instanceIdResult.getToken();
                new SessionManager(getBaseContext()).saveFCMToken(fcmToken);
                Log.d(TAG, "onSuccess: " + fcmToken);
            }
        });

        btnCreate.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnForgetPass.setOnClickListener(this);
    }


    @Override
    public void onSuccess(int requestCode, JSONObject json) {

        Log.e(TAG, "onSuccess: " + json);

        showLoading(false);

        switch (requestCode) {

            case VolleyHelper.REQ_CODE_LOGIN:

                boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);

                if (status) {

                    JSONObject jsonObject = json.optJSONObject(Constant.RESPONSE);
                    JSONObject userData = jsonObject.optJSONObject(Constant.USER_DATA);

                    if (Integer.parseInt(userData.optString(Constant.ROLE)) == 2) {

                        String walletA = "0.00";

                        if (!userData.optString(Constant.WALLET_AMOUNT).isEmpty()) {
                            walletA = userData.optString(Constant.WALLET_AMOUNT);
                        }

                        new SessionManager(this).createUserLoginSession(
                                userData.optString(Constant.ID),
                                userData.optString(Constant.FULL_NAME),
                                userData.optString(Constant.USER_MOBILE_NO),
                                userData.optString(Constant.USER_EMAIL),
                                json.optString(Constant.AUTH_TOKEN),
                                json.optString(Constant.TOKEN_TYPE),
                                userData.optBoolean(Constant.IS_ACTIVE),
                                userData.optString(Constant.REFER_CODE),
                                userData.optBoolean(Constant.IS_MOBILE_VERIFIED),
                                userData.optString(Constant.PROFILE_PIC),
                                userData.optString(Constant.ADDRESS),
                                walletA);

                        if (userData.optBoolean(Constant.IS_MOBILE_VERIFIED)) {
                            finish();
                        } else {
                            Toast.makeText(this, "Please Verify you mobile number", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                            intent.putExtra(Constant.USER_MOBILE_NO, userData.optString(Constant.USER_MOBILE_NO));
                            intent.putExtra(Constant.AUTH_TOKEN, json.optString(Constant.AUTH_TOKEN));
                            intent.putExtra(Constant.USER_DATA, userData + "");
                            startActivity(intent);
                        }

                     /*   Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
*/


                    } else
                        Snackbar.make(etMobile, getResources().getString(R.string.unauthorized_user), Snackbar.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {

        showLoading(false);
        Snackbar.make(etMobile, message, Snackbar.LENGTH_LONG).show();

        Log.e(TAG, "onError: " + message);

    }

    @Override
    public void onCanceled(int requestCode, String message) {

        showLoading(false);
        Snackbar.make(etMobile, message, Snackbar.LENGTH_LONG).show();

        Log.e(TAG, "onCanceled: " + message);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:
                hideKeyboard();
                maintainerror();

                String mobileNo = etMobile.getEditText().getText().toString().trim();
                String password = etPass.getEditText().getText().toString().trim();

                View focusView = null;
                Boolean isCancel = false;

                if (password.equals("")) {

                    focusView = etPass;
                    isCancel = true;
                    etPass.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isValidPassword(password)) {

                    focusView = etPass;
                    isCancel = true;
                    etPass.setError(getResources().getString(R.string.error_pass));

                } else if (mobileNo.equals("")) {

                    focusView = etMobile;
                    isCancel = true;
                    etMobile.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isValidMobile(mobileNo)) {

                    focusView = etMobile;
                    isCancel = true;
                    etMobile.setError(getResources().getString(R.string.error_mobile));

                }

                if (isCancel) {

                    if (focusView != null) {
                        focusView.requestFocus();
                    }

                } else {

                    if (ConnectionUtil.isInternetOn(getBaseContext())) {
                        showLoading(true);
                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_LOGIN, getBaseContext(), new VolleyHelper().getLoginParam(mobileNo, password, new SessionManager(getBaseContext()).getFcmToken()), VolleyHelper.LOGIN_URL, LoginActivity.this, false);
                    } else {
                        Snackbar.make(etMobile, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                    }

                }
                break;

            case R.id.btn_SignUp:
                hideKeyboard();
                etMobile.getEditText().setText("");
                etPass.getEditText().setText("");

                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);

                break;

            case R.id.btn_forget_pass:
                hideKeyboard();
                etMobile.getEditText().setText("");
                etPass.getEditText().setText("");

                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;
        }
    }

    private void maintainerror() {

        etPass.setError(null);
        etMobile.setError(null);
        etPass.setErrorEnabled(false);
        etMobile.setErrorEnabled(false);

    }

}
