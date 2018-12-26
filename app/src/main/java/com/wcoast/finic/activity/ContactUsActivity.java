package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class ContactUsActivity extends BaseActivity implements VolleyResponseListener {
    private static final String TAG = "ContactUsActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.til_mobile_no)
    TextInputLayout etMobile;

    @BindView(R.id.et_message)
    TextInputLayout etMessage;

    @BindView(R.id.et_full_name)
    TextInputLayout etName;

    @BindView(R.id.btn_submit_message)
    Button btnSubmit;

    private String message, name, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getValues();
                maintainErrors();

                Boolean isCancel = false;
                View focusView = null;

                if (message.isEmpty()) {

                    focusView = etMessage;
                    isCancel = true;
                    etMessage.setError(getResources().getString(R.string.error_empty));

                } else if (mobile.isEmpty()) {

                    focusView = etMobile;
                    isCancel = true;
                    etMobile.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isValidMobile(mobile)) {

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
                        showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_CONTACT_US, getBaseContext(), new VolleyHelper().getContactUsParams(name, mobile, message), VolleyHelper.CONTACT_US_URL, ContactUsActivity.this, false);
                    } else {
                        Snackbar.make(etMobile, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void getValues() {

        message = etMessage.getEditText().getText().toString().trim();
        name = etName.getEditText().getText().toString().trim();
        mobile = etMobile.getEditText().getText().toString().trim();

    }

    private void maintainErrors() {

        etMessage.setError(null);
        etName.setError(null);
        etMobile.setError(null);
        etMessage.setErrorEnabled(false);
        etName.setErrorEnabled(false);
        etMobile.setErrorEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    @Override
    public void onSuccess(int requestCode, JSONObject json) {

        switch (requestCode) {

            case VolleyHelper.REQ_CODE_CONTACT_US:

                showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

                boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);

                if (status) {

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ContactUsActivity.this, HomeActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                } else Snackbar.make(etMobile, message, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Method will be called when error occurred during  volley request.
     *
     * @param requestCode
     * @param message
     */
    @Override
    public void onError(int requestCode, String message) {

        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

        Snackbar.make(etMobile, message, Snackbar.LENGTH_LONG).show();
        Log.e(TAG, "onError: " + message);
    }

    /**
     * Method will be called when volley request is canceled due to some reason.
     *
     * @param requestCode
     * @param message
     */
    @Override
    public void onCanceled(int requestCode, String message) {

        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

        Snackbar.make(etMobile, message, Snackbar.LENGTH_LONG).show();
        Log.e(TAG, "onError: " + message);
    }
}
