package com.wcoast.finic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.ReferredPeopleAdapter;
import com.wcoast.finic.model.ModelReferredPeople;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferActivitySubTree extends BaseActivity implements VolleyResponseListener {

    private static final String TAG = "ReferActivitySubTree";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_user_image)
    ImageView ivUserImage;

    @BindView(R.id.rv_referrals)
    RecyclerView rvReferrals;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.tv_name_initials)
    TextView tvInitials;

    @BindView(R.id.tv_level_no)
    TextView tvLevelNumber;

    @BindView(R.id.layout_referal)
    RelativeLayout layoutReferral;
    ReferredPeopleAdapter referredPeopleAdapter;
    ArrayList<ModelReferredPeople> referredPeopleArrayList = new ArrayList<>();
    ArrayList<Integer> idStack = new ArrayList<>();
    int userId;
    Boolean callApi = true;
    private int levelNo;
    private JSONArray children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_sub_tree);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("Your Referral Tree");
        if (getIntent().hasExtra(Constant.USER_ID)) {

            userId = Integer.parseInt(getIntent().getStringExtra(Constant.USER_ID));
            if (userId == 0) {
                callApi = false;
            }

            idStack = getIntent().getIntegerArrayListExtra(Constant.ID);
            levelNo = getIntent().getIntExtra(Constant.LEVEL_NO, 0);
            Log.d(TAG, "onCreate: levelno " + levelNo);
            // levelNo = levelNo + 1;
        } else {

            userId = Integer.parseInt(new SessionManager(this).getUserId());
            levelNo = 1;
        }

        tvLevelNumber.setText(String.format(Locale.ENGLISH, "Level %d", levelNo));

        idStack.add(userId);

        if (callApi) {
            if (ConnectionUtil.isInternetOn(this)) {

                layoutReferral.setVisibility(View.GONE);
                showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
                VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_GET_REFERRALS, this, new VolleyHelper().getReferralparams(userId + "", levelNo), VolleyHelper.GET_REFERRALS_URL, this, false);

            } else
                Snackbar.make(rvReferrals, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
        } else {
            //set placeholder on user profile image.
            tvInitials.setVisibility(View.VISIBLE);
            ivUserImage.setVisibility(View.GONE);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            tvInitials.setBackgroundColor(color);
            tvInitials.setText(getInitials("Not Available"));

        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvReferrals.setLayoutManager(mLayoutManager);
        rvReferrals.setItemAnimator(new DefaultItemAnimator());

        referredPeopleAdapter = new ReferredPeopleAdapter(this);
        referredPeopleAdapter.setData(referredPeopleArrayList, levelNo, idStack);

        rvReferrals.setAdapter(referredPeopleAdapter);

    }

    @Override
    public void onSuccess(int requestCode, JSONObject json) {

        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

        switch (requestCode) {
            case VolleyHelper.REQ_CODE_GET_REFERRALS:
                Boolean status = json.optBoolean(Constant.STATUS);

                if (status) {

                    layoutReferral.setVisibility(View.VISIBLE);

                    JSONObject response = json.optJSONObject(Constant.RESPONSE);
                    JSONObject userData = response.optJSONObject(Constant.USER_DATA);

                    String profilePic = userData.optString(Constant.PROFILE_PIC);

                    if (profilePic.isEmpty()) {

                        tvInitials.setVisibility(View.VISIBLE);
                        ivUserImage.setVisibility(View.GONE);

                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        tvInitials.setBackgroundColor(color);
                        tvInitials.setText(getInitials(userData.optString(Constant.FULL_NAME)));

                    } else {
                        tvInitials.setVisibility(View.GONE);
                        ivUserImage.setVisibility(View.VISIBLE);
                        Picasso.get().load(profilePic).into(ivUserImage);
                    }

                    children = userData.optJSONArray(Constant.CHILDS);
                    int childCount = children.length();
/*
                    if (childCount != 0) {
                           }

                } else {
                    tvEmpty.setVisibility(View.VISIBLE);
                    layoutReferral.setVisibility(View.GONE);
                }*/
                    try {
                        subTreeArrayList(childCount);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rvReferrals.setAdapter(referredPeopleAdapter);
                    referredPeopleAdapter.setData(referredPeopleArrayList, levelNo, idStack);
                    referredPeopleAdapter.notifyDataSetChanged();
                }
                break;
        }

    }

    public void subTreeArrayList(int count) throws JSONException {

        for (int i = 0; i < count; i++) {

            JSONObject subTreeData = children.getJSONObject(i);
            String userId = subTreeData.optString(Constant.ID);
            String name = subTreeData.optString(Constant.FULL_NAME);
            String mobile = subTreeData.optString(Constant.USER_MOBILE_NO);
            String date = subTreeData.optString(Constant.REFER_DATE);
            String email = subTreeData.optString(Constant.USER_EMAIL);
            String profilePic = subTreeData.optString(Constant.PROFILE_PIC);
            String childAdded = subTreeData.optString(Constant.CHILD_ADDED);
            String childTotal = subTreeData.optString(Constant.CHILD_TOTAL);
            String receiverFcmToken = subTreeData.optString(Constant.FCM_TOKEN);
            String referToken = subTreeData.optString(Constant.REFER_CODE);

            ModelReferredPeople referredPeople = new ModelReferredPeople(userId, name, mobile, email, date, profilePic, childAdded, childTotal, receiverFcmToken, referToken);
            referredPeopleArrayList.add(referredPeople);

        }
    }

    @Override
    public void onError(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        Snackbar.make(rvReferrals, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        Snackbar.make(rvReferrals, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {

        if (levelNo > 1) {

            try {
                levelNo = levelNo - 1;
                Log.d(TAG, "onBackPressed: levelno " + levelNo);
                Intent in = new Intent(ReferActivitySubTree.this, ReferActivitySubTree.class);
                in.putExtra(Constant.LEVEL_NO, levelNo);

                idStack.remove(idStack.size() - 1);
                in.putExtra(Constant.USER_ID, idStack.get(idStack.size() - 1) + "");

                idStack.remove(idStack.size() - 1);
                in.putExtra(Constant.ID, idStack);

                in.putExtra(getIntent().getStringExtra(Constant.FULL_NAME), "");
                startActivity(in);
            } catch (Exception e) {
                finish();
            }
        } else if (levelNo == 1) {
            super.onBackPressed();
        }
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private String getInitials(String subCatName) {
        StringBuilder initials = new StringBuilder();
        String[] words = subCatName.split(" ");

        if (words.length > 1) {
            for (int i = 0; i < 2; i++) {
                if (initials.length() == 0) {
                    initials = new StringBuilder(words[i].charAt(0) + "");
                } else
                    initials.append(" ").append(words[i].charAt(0));
            }
        } else {
            initials = new StringBuilder(words[0].charAt(0) + " " + words[0].charAt(1));
        }
        return initials.toString();
    }
}
