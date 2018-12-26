package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.wcoast.finic.Helper.KhanaKhazanaHelper;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.KhanaKhazanaImageAdapter;
import com.wcoast.finic.model.ModelKhanaKhazana;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KhanaKhazanaIImages extends AppCompatActivity {
    @BindView(R.id.rv_image_khana_khazana)
    RecyclerView rvImageKhanaKhazana;

    @BindView(R.id.btn_chat)
    Button btnChat;

    int categoryIndex; // The index is to define the position on which the chick to ies images from this activity has been made

    private KhanaKhazanaImageAdapter khanaKhazanaImageAdapter = new KhanaKhazanaImageAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khana_khazana_sub_category);
        ButterKnife.bind(this);

        categoryIndex = getIntent().getIntExtra(Constant.ID, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rvImageKhanaKhazana.setLayoutManager(new LinearLayoutManager(this));
        rvImageKhanaKhazana.setItemAnimator(new DefaultItemAnimator());

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvImageKhanaKhazana);

        ArrayList<ModelKhanaKhazana> khanaKhazanaArrayList = KhanaKhazanaHelper.getKhanaKhazanaList();
        toolbar.setTitle(khanaKhazanaArrayList.get(categoryIndex).getSubCategoryName());

        khanaKhazanaImageAdapter.setData(khanaKhazanaArrayList.get(categoryIndex).getImageNames());

        rvImageKhanaKhazana.setAdapter(khanaKhazanaImageAdapter);

        new SessionManager(this).addSubCatIDAndName(khanaKhazanaArrayList.get(categoryIndex).getSubCategoryId() + "", khanaKhazanaArrayList.get(categoryIndex).getSubCategoryName());// as all before categories are opened by using session manager.

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(KhanaKhazanaIImages.this, ChatActivity.class);
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
