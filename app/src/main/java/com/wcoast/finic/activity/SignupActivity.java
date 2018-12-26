package com.wcoast.finic.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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

public class SignupActivity extends BaseActivity implements VolleyResponseListener, View.OnClickListener {
    @BindView(R.id.til_mobile_no)
    TextInputLayout tilMobile;
    @BindView(R.id.til_password)
    TextInputLayout tilPass;
    @BindView(R.id.tv_terms_and_condition)
    TextView tvTermsAndCondition;
    @BindView(R.id.til_username)
    TextInputLayout tilName;
    @BindView(R.id.til_c_pass)
    TextInputLayout tilConfirmPass;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_refer_code)
    TextInputLayout tilReferCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_sign_up)
    Button btnsignup;
    @BindView(R.id.cb_approval)
    CheckBox cbTNC;
    private String TAG = SignupActivity.class.getSimpleName();
    private String mobileNo, password, name, emailId, cnfrmPass, fcmToken, refercode;
    private String referrerUid;

    private Boolean isPasswordShown = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        btnsignup.setOnClickListener(this);
        tvTermsAndCondition.setOnClickListener(this);

        if (!new SessionManager(this).getTempReferral().isEmpty()) {
            tilReferCode.getEditText().setText(new SessionManager(this).getTempReferral());
            tilReferCode.getEditText().setFocusable(false);
        }

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etPassword.getRight() - etPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (isPasswordShown) {
                            etPassword.setTransformationMethod(new PasswordTransformationMethod());
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.pwd_show, 0);
                            isPasswordShown = false;
                        } else {
                            etPassword.setTransformationMethod(null);
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.pwd_hide, 0);
                            isPasswordShown = true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void getValues() {
        mobileNo = tilMobile.getEditText().getText().toString().trim();
        emailId = tilEmail.getEditText().getText().toString().trim();
        cnfrmPass = tilConfirmPass.getEditText().getText().toString().trim();
        password = tilPass.getEditText().getText().toString().trim();
        name = tilName.getEditText().getText().toString().trim();
        refercode = tilReferCode.getEditText().getText().toString().trim();
        fcmToken = new SessionManager(getBaseContext()).getFcmToken();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                getValues();
                maintainError();

                View focusView = null;
                Boolean isCancel = false;

                if (password.isEmpty()) {
                    focusView = tilPass;
                    isCancel = true;
                    tilPass.setError(getResources().getString(R.string.error_empty));
                } else if (!Validation.isValidPassword(password)) {
                    focusView = tilPass;
                    isCancel = true;
                    tilPass.setError(getResources().getString(R.string.error_pass));
                } else if (mobileNo.isEmpty()) {
                    focusView = tilMobile;
                    isCancel = true;
                    tilMobile.setError(getResources().getString(R.string.error_empty));
                } else if (!Validation.isValidMobile(mobileNo)) {
                    focusView = tilMobile;
                    isCancel = true;
                    tilMobile.setError(getResources().getString(R.string.error_mobile));
                } else if (name.isEmpty()) {
                    focusView = tilName;
                    isCancel = true;
                    tilName.setError(getResources().getString(R.string.error_empty));
                } else if (cnfrmPass.isEmpty()) {
                    focusView = tilConfirmPass;
                    isCancel = true;
                    tilConfirmPass.setError(getResources().getString(R.string.error_empty));
                } else if (!Validation.isSamePassword(cnfrmPass, password)) {
                    focusView = tilConfirmPass;
                    isCancel = true;
                    tilConfirmPass.setError(getResources().getString(R.string.error_pass_dont_match));
                } else if (!cbTNC.isChecked()) {
                    isCancel = true;
                    Snackbar.make(tilMobile, getResources().getString(R.string.text_tnc_approval), Snackbar.LENGTH_SHORT).show();
                }

                if (isCancel) {
                    if (focusView != null) {
                        focusView.requestFocus();
                    }
                } else {

                    if (ConnectionUtil.isInternetOn(getBaseContext())) {
                        showLoading(true);
                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_SIGN_UP, getBaseContext(), new VolleyHelper().getSignUpParam(name, mobileNo, password, emailId, refercode, fcmToken), VolleyHelper.SIGN_UP_URL, SignupActivity.this, false);
                    } else {
                        Snackbar.make(tilMobile, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.tv_terms_and_condition:
                sendToTermsAndCondition();
                break;
        }
    }

    private void maintainError() {
        tilPass.setError(null);
        tilMobile.setError(null);
        tilConfirmPass.setError(null);
        tilEmail.setError(null);
        tilName.setError(null);
        tilPass.setErrorEnabled(false);
        tilMobile.setErrorEnabled(false);
        tilConfirmPass.setErrorEnabled(false);
        tilEmail.setErrorEnabled(false);
        tilName.setErrorEnabled(false);

    }

    @Override
    public void onSuccess(int requestCode, JSONObject json) {
        switch (requestCode) {
            case VolleyHelper.REQ_CODE_SIGN_UP:

                showLoading(false);

                boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);
                String authToken = json.optString(Constant.AUTH_TOKEN);

                if (status) {

                    JSONObject jsonObject = json.optJSONObject(Constant.RESPONSE);
                    JSONObject userData = jsonObject.optJSONObject(Constant.USER_DATA);

                    tilPass.getEditText().setText("");
                    tilConfirmPass.getEditText().setText("");

                    Intent in = new Intent(SignupActivity.this, ActivateAccAct.class);
                    in.putExtra(Constant.USER_DATA, userData + "");
                    in.putExtra(Constant.AUTH_TOKEN, authToken + "");
                    in.putExtra(Constant.REFER_CODE, refercode);
                    in.putExtra(Constant.USER_MOBILE_NO, mobileNo);
                    startActivity(in);

                } else Snackbar.make(tilMobile, message, Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onError(int requestCode, String message) {
        showLoading(false);
        Snackbar.make(tilMobile, message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false);
        Snackbar.make(tilMobile, message, Snackbar.LENGTH_LONG).show();
    }
    private void sendToTermsAndCondition(){
        Intent in = new Intent(SignupActivity.this, TermsAndConditionsActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }
}
