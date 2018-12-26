package com.wcoast.finic.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationActivity extends BaseActivity implements VolleyResponseListener, View.OnClickListener {
    private static final String TAG = VerificationActivity.class.getSimpleName();

    @BindView(R.id.btn_verify_otp)
    Button btnVerifyOtp;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.pv_otp)
    PinView pvOtp;
    @BindView(R.id.tv_resend_otp)
    TextView tvResendOTP;
    private String mobile, authToken;
    private JSONObject userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);

        mobile = getIntent().getStringExtra(Constant.USER_MOBILE_NO);
        authToken = getIntent().getStringExtra(Constant.AUTH_TOKEN);

        try {
            String dataString = getIntent().getStringExtra(Constant.USER_DATA);
            userData = new JSONObject(dataString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnLogout.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
        tvResendOTP.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:

                new SessionManager(getBaseContext()).logoutUser();
                Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

            case R.id.btn_verify_otp:

                String OTP = pvOtp.getText().toString();
                if (OTP.isEmpty()) {
                    Snackbar.make(pvOtp, getString(R.string.text_enter_otp), Snackbar.LENGTH_SHORT).show();
                } else {
                    if (ConnectionUtil.isInternetOn(getBaseContext())) {
                        showLoading(true);
                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_VERIFY_USER, getBaseContext(), new VolleyHelper().getVerificationParams(mobile, OTP), VolleyHelper.VERIFY_USER_URL, VerificationActivity.this, false);
                    } else {
                        Snackbar.make(pvOtp, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.tv_resend_otp:

                if (ConnectionUtil.isInternetOn(getBaseContext())) {
                    showLoading(true);
                    VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_FORGET_PASS, getBaseContext(), new VolleyHelper().getForgetPassParam(mobile), VolleyHelper.FORGET_PASSWORD_URL, VerificationActivity.this, false);
                } else {
                    Snackbar.make(pvOtp, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                }
                break;
        }

    }


    @Override
    public void onSuccess(int requestCode, JSONObject json) {
        switch (requestCode) {
            case VolleyHelper.REQ_CODE_FORGET_PASS:
                showLoading(false);

                boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);

                if (status) {
                    final Dialog dialog = new Dialog(VerificationActivity.this, R.style.Theme_AppCompat_Dialog);
                    dialog.setContentView(R.layout.dialog_enter_otp);

                    Button verify = dialog.findViewById(R.id.btn_verify);

                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            Intent in = new Intent(VerificationActivity.this, VerificationActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(in);
                        }
                    });
                    dialog.show();

                } else Snackbar.make(pvOtp, message, Snackbar.LENGTH_SHORT).show();
                break;

            case VolleyHelper.REQ_CODE_VERIFY_USER:
                showLoading(false);

                boolean status1 = json.optBoolean(Constant.STATUS);
                String message1 = json.optString(Constant.MESSAGE);

                if (status1) {
                    new SessionManager(this).createUserLoginSession(userData.optString(Constant.ID),
                            userData.optString(Constant.FULL_NAME),
                            userData.optString(Constant.USER_MOBILE_NO),
                            userData.optString(Constant.USER_EMAIL),
                            authToken,
                            "Bearer",
                            userData.optBoolean(Constant.IS_ACTIVE),
                            userData.optString(Constant.REFER_CODE),
                            userData.optBoolean(Constant.IS_MOBILE_VERIFIED),
                            userData.optString(Constant.PROFILE_PIC),
                            userData.optString(Constant.ADDRESS),
                            userData.optString(Constant.WALLET_AMOUNT));

                    Intent in = new Intent(VerificationActivity.this, HomeActivity.class);
                    in.putExtra(Constant.USER_DATA, userData + "");
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } else Snackbar.make(pvOtp, message1, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        showLoading(false);
        Snackbar.make(pvOtp, getResources().getString(R.string.try_again), Snackbar.LENGTH_SHORT).show();

    }


    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false);
        Snackbar.make(pvOtp, getResources().getString(R.string.try_again), Snackbar.LENGTH_SHORT).show();
    }
}
