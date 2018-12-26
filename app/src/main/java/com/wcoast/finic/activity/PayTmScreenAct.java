package com.wcoast.finic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.response.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONObject;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class PayTmScreenAct implements VolleyResponseListener {
    private static final String TAG = "PayTmScreenAct";
    PaytmPGService Service = null;
    private Context context;
    private String hashKey, orderID, txnAmount, userId, mobileNo, userData, referCode;

    public PayTmScreenAct(Context context) {
        this.context = context;

    }

    public void PaytmData(String hashKey, String orderID,String userId, String txnAmount, String mobile, String userData, String referCode) {
        this.orderID = orderID;
        this.txnAmount = txnAmount;
        this.userId = userId;
        this.userData = userData;
        this.mobileNo = mobile;
        this.referCode = referCode;

    }



 /*   public void startPaytm() {

        Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("MID", "dummy");
        paramMap.put("ORDER_ID", orderID);
        paramMap.put("CUST_ID", userId);
        paramMap.put("INDUSTRY_TYPE_ID", "dummy");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("TXN_AMOUNT", txnAmount);
        paramMap.put("WEBSITE", "dummy");
        paramMap.put("CALLBACK_URL", callbackUrl);
        paramMap.put("CHECKSUMHASH", checksumHash);

        PaytmOrder Order = new PaytmOrder(paramMap);

        Log.d(TAG, "startPaytm: " + paramMap.toString());

        Service.initialize(Order, null);

        Service.startPaymentTransaction(context, true, true, new PaytmPaymentTransactionCallback() {
            @Override
            public void onTransactionResponse(Bundle inResponse) {

                Log.d(TAG, "onTransactionResponse: " + inResponse);

                if (inResponse.containsKey("RESPCODE")&& inResponse.getString("RESPCODE").equals("01") && inResponse.getString("STATUS").equals("TXN_SUCCESS")) {

                    String transactionID = inResponse.getString("TXNID");
                    String orderId = inResponse.getString("ORDERID");
                    String transactionDate = inResponse.getString("TXNDATE");
                    String transactionAmount = inResponse.getString("TXNAMOUNT");

                    VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_ACCOUNT_ACTIVATED, context, new VolleyHelper().getPaytmActivationparams(true, userId, orderId, transactionID, transactionDate, transactionAmount, referCode), VolleyHelper.ACCOUNT_ACTIVATE_URL, PayTmScreenAct.this, false);
                } else {
                    Toast.makeText(context, "Some error occured. Pl", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void networkNotAvailable() {
                Toast.makeText(context, " Network not available ", Toast.LENGTH_LONG).show();

            }

            @Override
            public void clientAuthenticationFailed(String inErrorMessage) {
                Toast.makeText(context, " Authentication Failed. " + inErrorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void someUIErrorOccurred(String inErrorMessage) {
                Toast.makeText(context, " UI Error Occur. " + inErrorMessage, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                Toast.makeText(context, " Error loading page. " + inErrorMessage, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBackPressedCancelTransaction() {
                Toast.makeText(context, " Transaction cancelled", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                Toast.makeText(context, "  Transaction cancelled ", Toast.LENGTH_LONG).show();

            }

        });
    }*/

    @Override
    public void onSuccess(int requestCode, JSONObject json) {
        switch (requestCode) {
            case VolleyHelper.REQ_CODE_ACCOUNT_ACTIVATED:
                Boolean status = json.optBoolean(Constant.STATUS);

                if (status) {

                  /*  Intent in = new Intent(context, VerificationActivity.class);
                    in.putExtra(Constant.USER_DATA, userData + "");
                    in.putExtra(Constant.AUTH_TOKEN, authToken + "");
                    in.putExtra(Constant.USER_MOBILE_NO, mobileNo);
                    context.startActivity(in);*/

                } else
                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public void onError(int requestCode, String message) {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onCanceled(int requestCode, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }
}
