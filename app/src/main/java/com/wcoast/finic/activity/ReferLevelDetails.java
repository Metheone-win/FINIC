package com.wcoast.finic.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.LevelDetailsAdapter;
import com.wcoast.finic.model.ModelLevelDetails;
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

public class ReferLevelDetails extends BaseActivity implements VolleyResponseListener {

    private static final String TAG = "ReferLevelDetails";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_level_details)
    RecyclerView rv_LevelDetails;

    @BindView(R.id.tv_total_earn_point)
    TextView tvTotalEarnPoint;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    ArrayList<ModelLevelDetails> modelLevelDetails = new ArrayList<>();
    private LevelDetailsAdapter levelDetailsAdapter;
    private int totalPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_level_details);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (ConnectionUtil.isInternetOn(this)) {
            showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);

            VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_LEVEL_DETAILS, this, new VolleyHelper().getUserIdParams(new SessionManager(this).getUserId()), VolleyHelper.LEVEL_DETAILS_URL, this, false);
        } else
            Snackbar.make(rv_LevelDetails, getResources().getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();

        levelDetailsAdapter = new LevelDetailsAdapter(this);

        rv_LevelDetails.setLayoutManager(new LinearLayoutManager(this));
        rv_LevelDetails.setItemAnimator(new DefaultItemAnimator());

        rv_LevelDetails.setAdapter(levelDetailsAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onSuccess(int requestCode, JSONObject json) {

        if (requestCode == VolleyHelper.REQ_LEVEL_DETAILS) {

            showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
            Boolean status = json.optBoolean(Constant.STATUS);
            String message = json.optString(Constant.MESSAGE);

            if (status) {

                JSONArray response = json.optJSONArray(Constant.RESPONSE);

                if (response != null) {

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    Type type = new TypeToken<List<ModelLevelDetails>>() {
                    }.getType();

                    modelLevelDetails = gson.fromJson(response.toString(), type);
                    levelDetailsAdapter.setData(modelLevelDetails);
                    for (int i = 0; i< modelLevelDetails.size(); i++){
                        totalPoint = totalPoint+Integer.parseInt(modelLevelDetails.get(i).getPointAdded());
                        Log.e(TAG,"point = "+modelLevelDetails.get(i).getPointAdded() +" total = "+totalPoint);
                    }

                    tvTotalEarnPoint.setText(String.format("%s %s", getString(R.string.total_point), String.valueOf(totalPoint)));
                } else
                    tvEmpty.setVisibility(View.VISIBLE);
            } else
                Snackbar.make(rv_LevelDetails, message, Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public void onError(int requestCode, String message) {
        Snackbar.make(rv_LevelDetails, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCanceled(int requestCode, String message) {
        Snackbar.make(rv_LevelDetails, message, Snackbar.LENGTH_LONG).show();
    }
}
