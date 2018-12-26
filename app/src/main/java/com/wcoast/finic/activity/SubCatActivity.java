package com.wcoast.finic.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.SQlite.DataBaseHelper;
import com.wcoast.finic.adapter.SubCatAdapter;
import com.wcoast.finic.model.ModelSubCategory;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCatActivity extends AppCompatActivity {
    @BindView(R.id.rv_subCat)
    RecyclerView rvSubCategory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private String TAG = SubCatActivity.class.getSimpleName();
    private ArrayList<ModelSubCategory> subCategoryList = new ArrayList<>();
    private SQLiteDatabase sqLiteDatabase;
    private String catInfo;

    private SubCatAdapter subCatAdapter;

    private String catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int catId = getIntent().getIntExtra(Constant.CAT_ID, 0);
        catName = getIntent().getStringExtra(Constant.CAT_NAME);
        catInfo = getIntent().getStringExtra(Constant.CAT_INFO_TEXT);

        toolbar.setTitle(catName);

        subCatAdapter = new SubCatAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvSubCategory.setLayoutManager(mLayoutManager);
        rvSubCategory.setItemAnimator(new DefaultItemAnimator());

        rvSubCategory.setAdapter(subCatAdapter);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

        subCategoryList.addAll(dataBaseHelper.getAllSubCategories(catId));
        Log.d(TAG, "onCreate: " + subCategoryList.size());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SubCatActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (new SessionManager(this).isUserLoggedIn()) {

            rvSubCategory.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);

            if (subCategoryList.size() == 0) {

                Log.d(TAG, "onCreate: " + subCategoryList.size());
                tvEmpty.setVisibility(View.VISIBLE);
            }

            subCatAdapter.setData(subCategoryList, catName);

        } else {

            rvSubCategory.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            tvEmpty.setText(catInfo);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
