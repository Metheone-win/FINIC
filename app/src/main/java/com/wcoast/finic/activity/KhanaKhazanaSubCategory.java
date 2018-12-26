package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcoast.finic.Helper.KhanaKhazanaHelper;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.KhanaKhazanaAdapter;
import com.wcoast.finic.model.ModelKhanaKhazana;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KhanaKhazanaSubCategory extends AppCompatActivity {
    @BindView(R.id.rv_subCat)
    RecyclerView rvImageKhanaKhazana;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private KhanaKhazanaAdapter khanaKhazanaAdapter = new KhanaKhazanaAdapter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("Khana Khazana");

        rvImageKhanaKhazana.setLayoutManager(new LinearLayoutManager(this));
        rvImageKhanaKhazana.setItemAnimator(new DefaultItemAnimator());

        rvImageKhanaKhazana.setAdapter(khanaKhazanaAdapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(KhanaKhazanaSubCategory.this, LoginActivity.class);
                startActivity(intent);

            }
        });
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

            rvImageKhanaKhazana.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            ArrayList<ModelKhanaKhazana> khanaKhazanaArrayList = new ArrayList<>();
            khanaKhazanaArrayList.clear();

            khanaKhazanaArrayList = KhanaKhazanaHelper.getKhanaKhazanaList();
            khanaKhazanaAdapter.setData(khanaKhazanaArrayList);

        } else {

            rvImageKhanaKhazana.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            tvEmpty.setText("The category lets you explore various food Receipes. Please Login in to continue");
        }
    }
}
