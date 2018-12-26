package com.wcoast.finic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.Helper.ArrayListHelper;
import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelNameImageLink;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuidanceSubCategories extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = GuidanceSubCategories.class.getSimpleName();
    @BindView(R.id.iv_category_image)
    ImageView ivCategoryImage;
    @BindView(R.id.iv_arrow_reference)
    ImageView ivArrowReference;

    @BindView(R.id.iv_category_image2)
    ImageView ivCategoryImage2;
    @BindView(R.id.iv_arrow_reference2)
    ImageView ivArrowReference2;

    @BindView(R.id.iv_category_image3)
    ImageView ivCategoryImage3;
    @BindView(R.id.iv_arrow_reference3)
    ImageView ivArrowReference3;

    @BindView(R.id.tv_sub_cat)
    TextView tvSubCategoryName;
    @BindView(R.id.tv_sub_cat2)
    TextView tvSubCategoryName2;
    @BindView(R.id.tv_sub_cat3)
    TextView tvSubCategoryName3;

    @BindView(R.id.btn_chat)
    Button btnChat;

    private String subCatId;
    private String subCatName;

    Intent intent = null;
    ArrayList<ModelNameImageLink> imageNameLinkArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance_sub_categories);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        switch (getIntent().getStringExtra(Constant.TAG)) {
            case Constant.EDUCATION:

                imageNameLinkArray = new ArrayListHelper().getEducationImage();
                toolbar.setTitle(Constant.EDUCATION);

                subCatId = "14";
                subCatName = Constant.EDUCATION;


                break;
            case Constant.CAREER:

                imageNameLinkArray = new ArrayListHelper().getCareerImage();
                toolbar.setTitle(Constant.CAREER);
                subCatId = "15";
                subCatName = Constant.CAREER;

                break;
            case Constant.BUSINESS:

                imageNameLinkArray = new ArrayListHelper().getBusinessImage();
                toolbar.setTitle(Constant.BUSINESS);
                subCatId = "16";
                subCatName = Constant.BUSINESS;

                break;
        }

        new SessionManager(this).addSubCatIDAndName(subCatId, subCatName);

        int img1 = getResources().getIdentifier(imageNameLinkArray.get(0).getImageName(), "drawable", getPackageName());
        int img2 = getResources().getIdentifier(imageNameLinkArray.get(1).getImageName(), "drawable", getPackageName());
        int img3 = getResources().getIdentifier(imageNameLinkArray.get(2).getImageName(), "drawable", getPackageName());

        ivCategoryImage.setImageResource(img1);
        ivCategoryImage2.setImageResource(img2);
        ivCategoryImage3.setImageResource(img3);

        tvSubCategoryName.setText(imageNameLinkArray.get(0).getName());
        tvSubCategoryName2.setText(imageNameLinkArray.get(1).getName());
        tvSubCategoryName3.setText(imageNameLinkArray.get(2).getName());

        ivArrowReference.setOnClickListener(this);
        ivArrowReference2.setOnClickListener(this);
        ivArrowReference3.setOnClickListener(this);
        btnChat.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_arrow_reference:
                Log.e(TAG," Link = "+imageNameLinkArray.get(0).getLink());
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageNameLinkArray.get(0).getLink()));
                startActivity(intent);
                break;

            case R.id.iv_arrow_reference2:
                Log.e(TAG," Link = "+imageNameLinkArray.get(1).getLink());
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageNameLinkArray.get(1).getLink()));
                startActivity(intent);
                break;

            case R.id.iv_arrow_reference3:
                Log.e(TAG," Link = "+imageNameLinkArray.get(2).getLink());
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageNameLinkArray.get(2).getLink()));
                startActivity(intent);
                break;

            case R.id.btn_chat:
                intent = new Intent(GuidanceSubCategories.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
