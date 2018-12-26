package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineServices extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.cv_ticket_booking)
    CardView cvticketBooking;

    @BindView(R.id.cv_online_shopping)
    CardView cvOnlineShopping;

    @BindView(R.id.cv_online_games)
    CardView cvOnlineGames;

    @BindView(R.id.cv_bill_payment)
    CardView cvBillPayment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_services2);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(R.string.online_services);

        tvEmpty.setText(R.string.online_services_text);

        cvBillPayment.setOnClickListener(this);
        cvOnlineGames.setOnClickListener(this);
        cvticketBooking.setOnClickListener(this);
        cvOnlineShopping.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_ticket_booking:
                Intent intent1 = new Intent(OnlineServices.this, OnlineServicesSubCategory.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra(Constant.TAG, Constant.TICKET_BOOKING);
                startActivity(intent1);
                break;

            case R.id.cv_online_games:
                intent1 = new Intent(OnlineServices.this, OnlineServicesSubCategory.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra(Constant.TAG, Constant.ONLINE_GAMES);
                startActivity(intent1);
                break;

            case R.id.cv_online_shopping:
                intent1 = new Intent(OnlineServices.this, OnlineServicesSubCategory.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra(Constant.TAG, Constant.ONLINE_SHOPPING);
                startActivity(intent1);
                break;

            case R.id.cv_bill_payment:
                intent1 = new Intent(OnlineServices.this, OnlineServicesSubCategory.class);
                intent1.putExtra(Constant.TAG, Constant.BILL_PAYMENT);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                break;

            case R.id.btn_login:
                Intent intent = new Intent(OnlineServices.this, LoginActivity.class);
                startActivity(intent);

                break;
        }
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
        }
    }
}
