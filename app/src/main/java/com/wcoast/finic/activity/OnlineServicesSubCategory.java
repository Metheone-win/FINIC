package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wcoast.finic.Helper.ArrayListHelper;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.OnlineServiceSubcategoryAdapter;
import com.wcoast.finic.model.ModelNameLink;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineServicesSubCategory extends AppCompatActivity {
    @BindView(R.id.rv_online_service_sub_category)
    RecyclerView rvImages;

    @BindView(R.id.btn_chat)
    Button btnChat;

    private String subCatId;
    private String subCatName;

    private ArrayList<ModelNameLink> modelNameLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_services_sub_category);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        OnlineServiceSubcategoryAdapter serviceSubcategoryAdapter = new OnlineServiceSubcategoryAdapter(this);

        if (getIntent().getStringExtra(Constant.TAG).equals(Constant.TICKET_BOOKING)) {

            toolbar.setTitle(Constant.TICKET_BOOKING);
            modelNameLinks = new ArrayListHelper().getTicketBookingImageNames();
            subCatId = "10";
            subCatName = Constant.TICKET_BOOKING;

        } else if (getIntent().getStringExtra(Constant.TAG).equals(Constant.ONLINE_SHOPPING)) {

            toolbar.setTitle(Constant.ONLINE_SHOPPING);
            modelNameLinks = new ArrayListHelper().getOnlineShoppingImageNames();
            subCatId = "11";
            subCatName = Constant.ONLINE_SHOPPING;

        } else if (getIntent().getStringExtra(Constant.TAG).equals(Constant.ONLINE_GAMES)) {

            toolbar.setTitle(Constant.ONLINE_GAMES);
            modelNameLinks = new ArrayListHelper().getOnlineGameImageNames();
            subCatId = "12";
            subCatName = Constant.ONLINE_GAMES;

        } else if (getIntent().getStringExtra(Constant.TAG).equals(Constant.BILL_PAYMENT)) {

            toolbar.setTitle(Constant.BILL_PAYMENT);
            modelNameLinks = new ArrayListHelper().getBillPaymentImageNames();
            subCatId = "13";
            subCatName = Constant.BILL_PAYMENT;
        }

        new SessionManager(this).addSubCatIDAndName(subCatId, subCatName);// as all before categories are opened by using session manager.

        rvImages.setLayoutManager(new GridLayoutManager(this, 2));

        serviceSubcategoryAdapter.setData(modelNameLinks);
        rvImages.setAdapter(serviceSubcategoryAdapter);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(OnlineServicesSubCategory.this, ChatActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
