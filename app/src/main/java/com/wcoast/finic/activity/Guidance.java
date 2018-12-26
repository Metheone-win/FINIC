package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Guidance extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_education)
    ImageView ivEducation;
    @BindView(R.id.iv_career)
    ImageView ivCareer;
    @BindView(R.id.iv_business)
    ImageView ivBusiness;

    @BindView(R.id.logged_out_layout)
    View loggedOutLayout;

    @BindView(R.id.logged_in_layout)
    View loggedInLayout;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("Guidance");
        tvEmpty.setText(R.string.guidance_text);


        ivBusiness.setOnClickListener(this);
        ivCareer.setOnClickListener(this);
        ivEducation.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_education:
                intent = new Intent(this, GuidanceSubCategories.class);
                intent.putExtra(Constant.TAG, Constant.EDUCATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.iv_career:
                intent = new Intent(this, GuidanceSubCategories.class);
                intent.putExtra(Constant.TAG, Constant.CAREER);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.iv_business:
                intent = new Intent(this, GuidanceSubCategories.class);
                intent.putExtra(Constant.TAG, Constant.BUSINESS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.btn_login:

                Intent intent = new Intent(Guidance.this, LoginActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (new SessionManager(this).isUserLoggedIn()) {

            loggedOutLayout.setVisibility(View.GONE);
            loggedInLayout.setVisibility(View.VISIBLE);

        } else {
            loggedOutLayout.setVisibility(View.VISIBLE);
            loggedInLayout.setVisibility(View.GONE);
        }    }
}
