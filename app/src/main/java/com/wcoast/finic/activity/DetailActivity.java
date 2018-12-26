package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_detail)
    TextView textDetail;
    @BindView(R.id.text_detail_empty)
    TextView textDetailEmpty;
    @BindView(R.id.text_heading)
    TextView textHeading;
    @BindView(R.id.text_link)
    TextView textLink;
    @BindView(R.id.btn_chat)
    Button btnChat;
    private String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String subCatName = getIntent().getStringExtra(Constant.SUB_CAT_NAME);
        String subCatDetail = getIntent().getStringExtra(Constant.SUB_CAT_DETAIL);

        toolbar.setTitle(subCatName);

        textDetail.setMovementMethod(new ScrollingMovementMethod());

        textLink.setText(getIntent().getStringExtra(Constant.SUB_CAT_LINK));
        textHeading.setText(getIntent().getStringExtra(Constant.CAT_NAME));

        if (!subCatDetail.isEmpty()) {

            textDetailEmpty.setVisibility(View.INVISIBLE);
            textDetail.setText(subCatDetail);

        }

        new SessionManager(this).addSubCatIDAndName(getIntent().getStringExtra(Constant.SUB_CAT_ID), getIntent().getStringExtra(Constant.SUB_CAT_NAME));

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(DetailActivity.this, ChatActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
