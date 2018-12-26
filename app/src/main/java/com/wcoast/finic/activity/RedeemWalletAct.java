package com.wcoast.finic.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.RedeemHistoryAdapter;
import com.wcoast.finic.model.ModelBankDetails;
import com.wcoast.finic.model.ModelRedeemHistory;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedeemWalletAct extends BaseActivity implements VolleyResponseListener, View.OnClickListener {

    private static final String TAG = RedeemWalletAct.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.til_ifsc)
    TextInputLayout tilIfsc;

    @BindView(R.id.til_account_no)
    TextInputLayout tilAccountNo;

    @BindView(R.id.til_branch_name)
    TextInputLayout tilBranchName;

    @BindView(R.id.til_bank_name)
    TextInputLayout tilBankName;

    @BindView(R.id.til_name)
    TextInputLayout tilHolerName;

    @BindView(R.id.til_points)
    TextInputLayout tilPoints;

    @BindView(R.id.et_ifsc)
    EditText etIfsc;

    @BindView(R.id.rv_redeem_history)
    RecyclerView rvRedeemHistory;

    @BindView(R.id.et_account_no)
    EditText etAccountNo;

    @BindView(R.id.et_branch_name)
    EditText etBranchName;

    @BindView(R.id.et_bank_name)
    EditText etBankName;

    @BindView(R.id.et_name)
    EditText etHolerName;

    @BindView(R.id.et_points)
    EditText etPoints;

    @BindView(R.id.btn_edit)
    Button btnEdit;

    @BindView(R.id.btn_redeem)
    Button btnRedeem;

    @BindView(R.id.btn_wallet)
    Button btnWallet;

    @BindView(R.id.cv_3)
    CardView cvRedeem;

    Boolean btnSave = false;
    ArrayList<ModelRedeemHistory> modelRedeemHistories = new ArrayList<>();
    private View focusView;
    private String walletAmount;
    private RedeemHistoryAdapter redeemHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_wallet);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        walletAmount = new SessionManager(this).getUserDetails().get(Constant.WALLET_AMOUNT);

        btnEdit.setOnClickListener(this);
        btnRedeem.setOnClickListener(this);

        redeemHistoryAdapter = new RedeemHistoryAdapter(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvRedeemHistory.setLayoutManager(mLayoutManager);

        rvRedeemHistory.setItemAnimator(new DefaultItemAnimator());

        rvRedeemHistory.setAdapter(redeemHistoryAdapter);

        SpannableString ss1 = new SpannableString("Points\n" + new SessionManager(this).getUserDetails().get(Constant.WALLET_AMOUNT));
        ss1.setSpan(new RelativeSizeSpan(1.05f), 0, 7, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, 7, 0);// set color

        etPoints.setText(new SessionManager(this).getUserDetails().get(Constant.WALLET_AMOUNT));
        etPoints.setSelection(etPoints.getText().length());

        btnWallet.setText(ss1);
        btnWallet.setOnClickListener(this);

        getBankRedeemDetails();
    }

    private void getBankRedeemDetails() {

        if (ConnectionUtil.isInternetOn(this)) {

            showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
            VolleyRequestHelper.VolleyGetRequest(VolleyHelper.REQ_GET_BANK_DETAIL, this, null, VolleyHelper.GET_BANK_DETAIL, this, false);

        } else
            Snackbar.make(tilAccountNo, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_edit:

                if (!btnSave) {

                    etBranchName.setFocusableInTouchMode(true);
                    etIfsc.setFocusableInTouchMode(true);
                    etAccountNo.setFocusableInTouchMode(true);
                    etHolerName.setFocusableInTouchMode(true);
                    etBankName.setFocusableInTouchMode(true);

                    etBranchName.setFocusable(true);
                    etIfsc.setFocusable(true);
                    etAccountNo.setFocusable(true);
                    etHolerName.setFocusable(true);
                    etBankName.setFocusable(true);

                    etBranchName.setSelection(etBranchName.getText().length());
                    etAccountNo.setSelection(etAccountNo.getText().length());
                    etBankName.setSelection(etBankName.getText().length());
                    etHolerName.setSelection(etHolerName.getText().length());
                    etIfsc.setSelection(etIfsc.getText().length());

                    tilAccountNo.requestFocus();

                    btnSave = true;
                    btnEdit.setText(R.string.save);

                } else {

                    hideKeyboard();

                    saveBankDetail();
                }

                break;

            case R.id.btn_redeem:

                if (Integer.parseInt(etPoints.getText().toString().trim()) <= Integer.parseInt(walletAmount)) {

                    if (errors()) {
                        if (focusView != null) {
                            focusView.requestFocus();
                        }
                    } else {
                        if (ConnectionUtil.isInternetOn(this)) {

                            showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
                            VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_REDEEM_POINTS, this, new VolleyHelper().getRedeemPoints(etPoints.getText().toString().trim()), VolleyHelper.REDEEM_REQUEST, this, false);

                        } else
                            Snackbar.make(tilAccountNo, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                    }
                } else
                    Snackbar.make(tilAccountNo, "Redeem Points cannot be greater than earned points.", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    public void saveBankDetail() {

        if (errors()) {
            if (focusView != null) {
                focusView.requestFocus();
            }
        } else {
            if (ConnectionUtil.isInternetOn(this)) {

                ModelBankDetails bankDetails = new ModelBankDetails();

                bankDetails.setAccountNo(etAccountNo.getText().toString().trim());
                bankDetails.setAccountHolderName(etHolerName.getText().toString().trim());
                bankDetails.setBankName(etBankName.getText().toString().trim());
                bankDetails.setBranchName(etBranchName.getText().toString().trim());
                bankDetails.setIfscCode(etIfsc.getText().toString().trim());

                showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
                VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_SET_BANK_DETAIL, this, new VolleyHelper().setBankDetail(bankDetails), VolleyHelper.SET_BANK_DETAIL, this, false);

            } else
                Snackbar.make(tilAccountNo, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(int requestCode, JSONObject json) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        switch (requestCode) {

            case VolleyHelper.REQ_GET_BANK_DETAIL:

                Boolean status = json.optBoolean(Constant.STATUS);

                if (status) {

                    JSONObject response = json.optJSONObject(Constant.RESPONSE);
                    JSONObject userData = response.optJSONObject(Constant.USER_DATA);

                    if (userData.has(Constant.BANK_DETAILS)) {

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        ModelBankDetails bankDetails = gson.fromJson(userData.optString(Constant.BANK_DETAILS), ModelBankDetails.class);

                        etAccountNo.setText(bankDetails.getAccountNo());
                        etIfsc.setText(bankDetails.getIfscCode());
                        etHolerName.setText(bankDetails.getAccountHolderName());
                        etBranchName.setText(bankDetails.getBranchName());
                        etBankName.setText(bankDetails.getBankName());

                        etBranchName.setFocusable(false);
                        etIfsc.setFocusable(false);
                        etAccountNo.setFocusable(false);
                        etHolerName.setFocusable(false);
                        etBankName.setFocusable(false);

                        btnEdit.setText(R.string.edit);

                    } else {
                        btnSave = true;
                        btnEdit.setText(R.string.save);

                        tilAccountNo.requestFocus();
                    }

                    if (userData.has(Constant.REDEEM_HISTORY)) {

                        JSONArray redeemHistoryData = userData.optJSONArray(Constant.REDEEM_HISTORY);

                        if (redeemHistoryData != null) {

                            cvRedeem.setVisibility(View.VISIBLE);

                            GsonBuilder gsonBuilder2 = new GsonBuilder();
                            Gson gson2 = gsonBuilder2.create();
                            Type type = new TypeToken<List<ModelRedeemHistory>>() {
                            }.getType();

                            modelRedeemHistories = gson2.fromJson(redeemHistoryData.toString(), type);

                            redeemHistoryAdapter.setData(modelRedeemHistories);

                        }

                    } else cvRedeem.setVisibility(View.GONE);


                } else {
                    Snackbar.make(tilAccountNo, json.optString(Constant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                }

                break;

            case VolleyHelper.REQ_SET_BANK_DETAIL:

                Boolean status2 = json.optBoolean(Constant.STATUS);

                if (status2) {
                  /*  GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    ModelBankDetails bankDetails = gson.fromJson(json.optString(Constant.RESPONSE), ModelBankDetails.class);

                    etAccountNo.setText(bankDetails.getAccountNo());
                    etIfsc.setText(bankDetails.getIfscCode());
                    etHolerName.setText(bankDetails.getAccountHolderName());
                    etBranchName.setText(bankDetails.getBranchName());
                    etBankName.setText(bankDetails.getBankName());
                   */
                    etBranchName.setFocusable(false);
                    etIfsc.setFocusable(false);
                    etAccountNo.setFocusable(false);
                    etHolerName.setFocusable(false);
                    etBankName.setFocusable(false);

                    btnSave = false;
                    btnEdit.setText(R.string.edit);

                    Snackbar.make(tilAccountNo, json.optString(Constant.MESSAGE), Snackbar.LENGTH_SHORT).show();

                } else {
                    Snackbar.make(tilAccountNo, json.optString(Constant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                }

                break;

            case VolleyHelper.REQ_REDEEM_POINTS:

                Boolean status3 = json.optBoolean(Constant.STATUS);

                if (status3) {

                    Snackbar.make(tilAccountNo, R.string.redeem_request_generated, Snackbar.LENGTH_SHORT).show();

                    //   getBankRedeemDetails();

                } else {
                    Snackbar.make(tilAccountNo, json.optString(Constant.MESSAGE), Snackbar.LENGTH_SHORT).show();
                }

                break;
        }

    }

    @Override
    public void onError(int requestCode, String message) {

    }

    @Override
    public void onCanceled(int requestCode, String message) {

    }

    public Boolean errors() {

        tilHolerName.setError(null);
        tilAccountNo.setError(null);
        tilIfsc.setError(null);
        tilBranchName.setError(null);
        tilHolerName.setErrorEnabled(false);
        tilAccountNo.setErrorEnabled(false);
        tilIfsc.setErrorEnabled(false);
        tilBranchName.setErrorEnabled(false);
        tilBankName.setError(null);
        tilBankName.setErrorEnabled(false);

        Boolean isCancel = false;
        focusView = null;

        if (etBranchName.getText().toString().trim().isEmpty()) {
            isCancel = true;
            focusView = tilBranchName;
            tilBranchName.setError(getString(R.string.error_empty));
        }
        if (etBankName.getText().toString().trim().isEmpty()) {
            isCancel = true;
            focusView = tilBranchName;
            tilBankName.setError(getString(R.string.error_empty));
        }
        if (etIfsc.getText().toString().trim().isEmpty()) {
            isCancel = true;
            focusView = tilBranchName;
            tilIfsc.setError(getString(R.string.error_empty));
        }
        if (etHolerName.getText().toString().trim().isEmpty()) {
            isCancel = true;
            focusView = tilBranchName;
            tilHolerName.setError(getString(R.string.error_empty));
        }
        if (etAccountNo.getText().toString().trim().isEmpty()) {
            isCancel = true;
            focusView = tilBranchName;
            tilAccountNo.setError(getString(R.string.error_empty));
        }

        return isCancel;
    }

    @Override
    protected void onStart() {
        new SessionManager(this).putRunningStatus(false, false, false, true);

        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onPause() {

        new SessionManager(this).putRunningStatus(false, false, false, false);
        Log.d(TAG, "onPause: ");
        super.onPause();

    }
}
