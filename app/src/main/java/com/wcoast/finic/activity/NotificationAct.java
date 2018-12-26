package com.wcoast.finic.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.adapter.NotificationAdapter;
import com.wcoast.finic.model.ModelNotification;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAct extends BaseActivity implements VolleyResponseListener {
    private static final String TAG = "NotificationAct";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.rv_noti)
    RecyclerView rvNotification;
    ModelNotification notification;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<ModelNotification> notificationArrayList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;

    private int currentPage = 1, lastPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ConnectionUtil.isInternetOn(this)) {

            showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);
            VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_NOTIFICATION, this, new VolleyHelper().getNotificationParams("1"), VolleyHelper.NOTIFICATION_URL, this, false);

        } else {

            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getResources().getString(R.string.string_no_connection));
        }

        notificationAdapter = new NotificationAdapter(this);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        rvNotification.setLayoutManager(mLayoutManager);

        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(notificationAdapter);


        rvNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (currentPage != lastPage && mLayoutManager.findLastCompletelyVisibleItemPosition() >= (notificationArrayList.size() - 5)) {

                    if (ConnectionUtil.isInternetOn(getBaseContext())) {

                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_NOTIFICATION, getBaseContext(), new VolleyHelper().getNotificationParams((currentPage + 1) + ""), VolleyHelper.NOTIFICATION_URL, NotificationAct.this, false);

                    } else
                        Snackbar.make(tvEmpty, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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
            case VolleyHelper.REQ_CODE_NOTIFICATION:
                boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);

                if (status) {

                    JSONObject response = json.optJSONObject(Constant.RESPONSE);

                    try {
                        currentPage = Integer.parseInt(response.getString(Constant.CURRENT_PAGE));
                        lastPage = Integer.parseInt(response.getString(Constant.LAST_PAGE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray notificationData = response.optJSONArray(Constant.NOTIFICATION_DATA);

                    Log.d(TAG, "onSuccess: " + notificationData.length());

                    if (notificationData.length() != 0) {

                        Log.d(TAG, "onSuccess DATA: " + notificationData.length());
                        tvEmpty.setVisibility(View.INVISIBLE);
                        //       toolbar.setTitle("Notification (" + (notificationData.length()) + ")");

                        for (int i = 0; i < notificationData.length(); i++) {
                            JSONObject notificationJson = notificationData.optJSONObject(i);

                            notification = new ModelNotification();
                            String dateStr = notificationJson.optString(Constant.NOTIFICATION_CREATED_AT);

                            CharSequence niceDateStr = DateUtils.getRelativeTimeSpanString(getDateInMillis(dateStr), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

                            notification.setNotificationDate((String) niceDateStr);
                            notification.setNotificationDesc(notificationJson.optString(Constant.NOTIFICATION_MESSAGE));
                            notification.setNotificationTitle(notificationJson.optString(Constant.NOTIFICATION_TITLE));
                            notification.setNotificationId(notificationJson.optString(Constant.NOTIFICATION_ID));

                            notificationArrayList.add(notification);
                        }
                        Log.d(TAG, "onSuccess: " + notificationArrayList.toString());
                        notificationAdapter.setData(notificationArrayList);

                    } else {

                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        Snackbar.make(tvEmpty, message, Snackbar.LENGTH_LONG).show();

    }


    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        Snackbar.make(tvEmpty, message, Snackbar.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        new SessionManager(this).putRunningStatus(false, false, true, false);

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
