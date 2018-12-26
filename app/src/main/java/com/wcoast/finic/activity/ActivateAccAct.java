package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivateAccAct extends BaseActivity implements View.OnClickListener, VolleyResponseListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_pay)
    Button btnPay;

    @BindView(R.id.tv_addiction_world)
    TextView tvAddictionWorld;

    @BindView(R.id.tv_membership)
    TextView tvMembership;

    @BindView(R.id.tv_activation_amount)
    TextView tvActAmount;

    private String TAG = ActivateAccAct.class.getSimpleName();
    private String mobile, authToken, userId, activationAmount, referCode;
    private String userData;
    private String email;
    private String firstname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_acc);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        SpannableString text = new SpannableString(getResources().getString(R.string.addiction_world));
        text.setSpan(new RelativeSizeSpan(1.25f), 0, 15, 0);
        tvAddictionWorld.setText(text, TextView.BufferType.SPANNABLE);

        SpannableString text2 = new SpannableString(getResources().getString(R.string.join_membership));
        text2.setSpan(new RelativeSizeSpan(1.25f), 0, 27, 0);
        tvMembership.setText(text2, TextView.BufferType.SPANNABLE);

        /*
          Get details to send to verification screen after transaction to maintain session.
         */
        mobile = getIntent().getStringExtra(Constant.USER_MOBILE_NO);
        authToken = getIntent().getStringExtra(Constant.AUTH_TOKEN);
        if (getIntent().hasExtra(Constant.REFER_CODE)){
            if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.REFER_CODE)) || getIntent().getStringExtra(Constant.REFER_CODE) != null) {
                referCode = getIntent().getStringExtra(Constant.REFER_CODE);
            }
        }

        try {
            /*
              Get details to send back to server after paytm transaction
             */
            userData = getIntent().getStringExtra(Constant.USER_DATA);
            JSONObject data = new JSONObject(userData);
            email = data.getString(Constant.USER_EMAIL);
            firstname = data.getString(Constant.FULL_NAME);
            userId = data.getString(Constant.ID);
            mobile = data.getString(Constant.USER_MOBILE_NO);
            activationAmount = data.getString(Constant.ACTIVATION_AMOUNT);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvActAmount.setText(String.format("₹ %s", activationAmount));
        btnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (ConnectionUtil.isInternetOn(getBaseContext())) {
                    showLoading(true);
                    VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_CHECKSUM_PAYTM, getBaseContext(), new VolleyHelper().getChecksumParams(userId, activationAmount), VolleyHelper.CHECKSUM_PAYTM_URL, ActivateAccAct.this, false);
                } else {
                    Snackbar.make(tvMembership, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void payUMoneyResponse(boolean status){
        if (ConnectionUtil.isInternetOn(getBaseContext())) {
            showLoading(true);
            VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_PAYUMONEY_RESPONSE, getBaseContext(), new VolleyHelper().payUmoneyResponseParams(status, userId, referCode), VolleyHelper.ACCOUNT_ACTIVATE_URL, ActivateAccAct.this, false);
        } else {
            Snackbar.make(tvMembership, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
        }
    }



    @Override
    public void onSuccess(int requestCode, JSONObject json) {
        showLoading(false);
        Log.e(TAG,"response json = "+json);
        switch (requestCode) {

            case VolleyHelper.REQ_CODE_CHECKSUM_PAYTM:
                Boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);
                Log.e(TAG, json.toString());

                if (status) {
                    JSONObject response = json.optJSONObject(Constant.RESPONSE);
                    String callbackUrl = response.optString(Constant.CALLBACK_URL);
                    String txnAmount = response.optString(Constant.TXN_AMOUNT);
                    String orderID = response.optString(Constant.ORDER_ID);
                    String checksumHash = response.optString(Constant.CHECKSUM_HASH);
                    startPayUMoney(checksumHash, orderID, txnAmount, callbackUrl);

                } else
                    Snackbar.make(btnPay, message, Snackbar.LENGTH_LONG).show();

                break;
            case VolleyHelper.REQ_PAYUMONEY_RESPONSE:
                Boolean status1 = json.optBoolean(Constant.STATUS);
                String message1 = json.optString(Constant.MESSAGE);
                Log.e(TAG, json.toString());

                if (status1) {
                    JSONObject response = json.optJSONObject(Constant.RESPONSE);
                    JSONObject userData = response.optJSONObject(Constant.USER_DATA);
                    Log.e(TAG,"response = "+response);
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
                        Intent intent = new Intent(this, VerificationActivity.class);
                        intent.putExtra(Constant.USER_MOBILE_NO, userData.optString(Constant.USER_MOBILE_NO));
                        intent.putExtra(Constant.AUTH_TOKEN, json.optString(Constant.AUTH_TOKEN));
                        intent.putExtra(Constant.USER_DATA, userData + "");
                        startActivity(intent);
                    }

                } else
                    Snackbar.make(btnPay, message1, Snackbar.LENGTH_LONG).show();
                break;
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
        showLoading(false);
        Snackbar.make(btnPay, message, Snackbar.LENGTH_LONG).show();

    }

    /**
     * Method will be called when volley request is canceled due to some reason.
     *
     * @param requestCode
     * @param message
     */
    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false);
        Snackbar.make(btnPay, message, Snackbar.LENGTH_LONG).show();

    }

    public void startPayUMoney(String hashKey, String orderId, String amount, String callbackUrl) {
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setKey("QIQatwZJ")
                .setTxnId(orderId)
                .setAmount(amount)
                .setProductName("Activatemlmaccount")
                .setFirstName(firstname)
                .setEmail(email)
                .setPhone(mobile)
                .setsUrl(callbackUrl)
                .setfUrl(callbackUrl)
                .setIsDebug(true)//should be false in production
                .setMerchantId(Constant.MERCHANT_ID);// using 6474895 this merchant id


        try {
            PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
            paymentParam.setMerchantHash(hashKey); // change hash key
            // Invoke the following function to open the checkout page.

            Log.d(TAG, "param: " + paymentParam.getParams().toString());

            PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, this, R.style.AppTheme, true);

        } catch (Exception e) {
            Log.e(TAG, "PayUMoneyException: " + e.getLocalizedMessage());
            Snackbar.make(btnPay, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                            .INTENT_EXTRA_TRANSACTION_RESPONSE);

                    ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

                    // Check which object is non-null
                    if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                        if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                            //Success Transaction

                            Log.e(TAG, "Success");
                            payUMoneyResponse(true);

                        } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.FAILED)){
                            //Failure Transaction
                            Toast.makeText(this, transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(TAG,transactionResponse.getMessage());
                            Log.e(TAG, "Failure");
                        }else {
                            Log.e(TAG, "other error");
                            Toast.makeText(this, transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // Response from Payumoney
                        String payuResponse = transactionResponse.getPayuResponse();
                         Log.e(TAG,"payuResponse = "+payuResponse);
                        // Response from SURl and FURL
                        String merchantResponse = transactionResponse.getTransactionDetails();
                        Log.e(TAG,"merchantResponse = "+merchantResponse);

                    } else if (resultModel != null && resultModel.getError() != null) {
                        Log.e("PAYU", "Error response : " + resultModel.getError().getTransactionResponse());
                        Toast.makeText(this, resultModel.getError().getTransactionResponse().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG,"data = "+ data);
                }

            } else {
                Log.e(TAG, "RESULT_CODE = " + resultCode);
            }
        }else {
            Log.e(TAG,"requestCode = "+ PayUmoneyFlowManager.REQUEST_CODE_PAYMENT);
        }

    }


}
