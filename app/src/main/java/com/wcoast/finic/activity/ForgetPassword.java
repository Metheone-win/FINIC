package com.wcoast.finic.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

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

public class ForgetPassword extends BaseActivity implements VolleyResponseListener, View.OnClickListener {
    @BindView(R.id.til_mobile_no)
    TextInputLayout etMobile;
    @BindView(R.id.btn_submit)
    Button btnSubmitMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ButterKnife.bind(this);
        btnSubmitMobile.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                String strMobile = etMobile.getEditText().getText().toString().trim();

                View focusView = null;
                Boolean isCancel = false;

                etMobile.setError(null);
                etMobile.setErrorEnabled(false);

                if (strMobile.equals("")) {

                    focusView = etMobile;
                    isCancel = true;
                    etMobile.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isValidMobile(strMobile)) {

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
                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_FORGET_PASS, getBaseContext(), new VolleyHelper().getForgetPassParam(strMobile), VolleyHelper.FORGET_PASSWORD_URL, ForgetPassword.this, false);
                    } else {
                        Snackbar.make(etMobile, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                    }
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

                    final Dialog dialog = new Dialog(ForgetPassword.this, R.style.Theme_AppCompat_Dialog);
                    dialog.setContentView(R.layout.dialog_enter_otp);
                    Button verify = dialog.findViewById(R.id.btn_verify);

                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            Intent in = new Intent(ForgetPassword.this, ChangePassAct.class);
                            etMobile.getEditText().setText("");
                            in.putExtra(Constant.USER_MOBILE_NO, etMobile.getEditText().getText().toString().trim());
                            startActivity(in);
                        }
                    });
                    dialog.show();

                } else
                    Snackbar.make(etMobile, "Mobile number does not exists!", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onError(int requestCode, String message) {

        Snackbar.make(etMobile, getResources().getString(R.string.try_again), Snackbar.LENGTH_SHORT).show();
        showLoading(false);
    }


    @Override
    public void onCanceled(int requestCode, String message) {

        Snackbar.make(etMobile, getResources().getString(R.string.try_again), Snackbar.LENGTH_SHORT).show();
        showLoading(false);
    }
}
