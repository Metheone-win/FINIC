package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.Validation;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePassAct extends BaseActivity implements VolleyResponseListener {

    private static final String TAG = "ChangePassAct";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pv_otp)
    PinView pvOTP;

    @BindView(R.id.et_new_pass)
    TextInputLayout etNewPass;

    @BindView(R.id.et_cnfrm_pass)
    TextInputLayout etConfirmPass;

    @BindView(R.id.btn_submit_new_password)
    Button btnSubmit;

    private String password, confirmPassword, mobileNo, otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mobileNo = getIntent().getStringExtra(Constant.USER_MOBILE_NO);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValues();
                maintainerror();

                Boolean isCancel = false;
                View focusView = null;

                if (otp.isEmpty()) {

                    isCancel = true;
                    focusView = pvOTP;
                    Snackbar.make(etNewPass, "Please enter OTP", Snackbar.LENGTH_SHORT).show();

                } else if (otp.length() != 4) {

                    isCancel = true;
                    focusView = pvOTP;
                    Snackbar.make(etNewPass, "Enter correct otp", Snackbar.LENGTH_SHORT).show();

                } else if (password.isEmpty()) {

                    isCancel = true;
                    focusView = etNewPass;
                    etNewPass.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isValidPassword(password)) {

                    isCancel = true;
                    focusView = etNewPass;
                    etNewPass.setError(getResources().getString(R.string.error_pass));


                } else if (confirmPassword.isEmpty()) {
                    isCancel = true;
                    focusView = etConfirmPass;
                    etConfirmPass.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isSamePassword(password, confirmPassword)) {

                    isCancel = true;
                    focusView = etConfirmPass;
                    etConfirmPass.setError(getResources().getString(R.string.error_pass_dont_match));

                }
                if (isCancel) {

                    if (focusView != null) {
                        focusView.requestFocus();
                    }

                } else {

                    if (ConnectionUtil.isInternetOn(getBaseContext())) {
                        showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_CHANGE_PASSWORD, getBaseContext(), new VolleyHelper().getChangePassParams(mobileNo, pvOTP.getText().toString(), password), VolleyHelper.CHANGE_PASSWORD_URL, ChangePassAct.this, false);
                    } else {
                        Snackbar.make(etNewPass, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                    }

                }
            }

        });
    }


    private void getValues() {

        password = etNewPass.getEditText().getText().toString().trim();
        confirmPassword = etConfirmPass.getEditText().getText().toString().trim();
        otp = pvOTP.getText().toString();

    }

    private void maintainerror() {

        etConfirmPass.setError(null);
        etNewPass.setError(null);
        etNewPass.setErrorEnabled(false);
        etConfirmPass.setErrorEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSuccess(int requestCode, JSONObject json) {

        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

        switch (requestCode) {
            case VolleyHelper.REQ_CODE_CHANGE_PASSWORD:

                boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);

                if (status) {

                    Toast.makeText(this, "Your Password has been successfully changed.", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ChangePassAct.this, LoginActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                } else Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

    }

    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

    }


}