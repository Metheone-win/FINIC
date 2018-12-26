package com.wcoast.finic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.wcoast.finic.Helper.ArrayListHelper;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.GamesAdapter;
import com.wcoast.finic.model.ModelNameLink;
import com.wcoast.finic.utility.Constant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GamesActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_game_image)
    ImageView ivGame1;

    @BindView(R.id.iv_game_image2)
    ImageView ivGame2;

    @BindView(R.id.iv_game_image3)
    ImageView ivGame3;

    @BindView(R.id.iv_game_image4)
    ImageView ivGame4;

    @BindView(R.id.iv_game_image5)
    ImageView ivGame5;

    @BindView(R.id.iv_game_image6)
    ImageView ivGame6;

    @BindView(R.id.cv_game_image6)
    CardView cvGame6;

    private GamesAdapter gamesAdapter = new GamesAdapter(this);

    private ArrayList<ModelNameLink> modelNameLinkArrayList;

    ArrayListHelper arrayListHelper = new ArrayListHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent().hasExtra(Constant.GAME_PS2)) {
            arrayListHelper.setDataForPS2();
            toolbar.setTitle(Constant.GAME_PS2);
            int game6 = getResources().getIdentifier(arrayListHelper.getModelNameLinkArrayList().get(5).getImageName(), "drawable", getPackageName());
            ivGame6.setImageResource(game6);

            ivGame6.setOnClickListener(this);


        } else if (getIntent().hasExtra(Constant.GAME_KINECT)) {
            arrayListHelper.setDataForKineet();
            toolbar.setTitle(Constant.GAME_KINECT);

            cvGame6.setVisibility(View.GONE);
            ivGame6.setVisibility(View.GONE);

        } else if (getIntent().hasExtra(Constant.GAME_XBOX)) {
            arrayListHelper.setDataForXBox();
            toolbar.setTitle(Constant.GAME_XBOX);
            cvGame6.setVisibility(View.GONE);
            ivGame6.setVisibility(View.GONE);

        }
        modelNameLinkArrayList = arrayListHelper.getModelNameLinkArrayList();

        //Get Image path directly and set in imageviews
        int game1 = getResources().getIdentifier(modelNameLinkArrayList.get(0).getImageName(), "drawable", getPackageName());
        int game2 = getResources().getIdentifier(modelNameLinkArrayList.get(1).getImageName(), "drawable", getPackageName());
        int game3 = getResources().getIdentifier(modelNameLinkArrayList.get(2).getImageName(), "drawable", getPackageName());
        int game4 = getResources().getIdentifier(modelNameLinkArrayList.get(3).getImageName(), "drawable", getPackageName());
        int game5 = getResources().getIdentifier(modelNameLinkArrayList.get(4).getImageName(), "drawable", getPackageName());

        ivGame1.setImageResource(game1);
        ivGame2.setImageResource(game2);
        ivGame3.setImageResource(game4);
        ivGame4.setImageResource(game3);
        ivGame5.setImageResource(game5);

        ivGame1.setOnClickListener(this);
        ivGame2.setOnClickListener(this);
        ivGame3.setOnClickListener(this);
        ivGame4.setOnClickListener(this);
        ivGame5.setOnClickListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_game_image:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLinkArrayList.get(0).getImageLink()));
                startActivity(intent);
                break;
            case R.id.iv_game_image2:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLinkArrayList.get(1).getImageLink()));
                startActivity(intent);
                break;
            case R.id.iv_game_image3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLinkArrayList.get(2).getImageLink()));
                startActivity(intent);
                break;
            case R.id.iv_game_image4:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLinkArrayList.get(3).getImageLink()));
                startActivity(intent);
                break;
            case R.id.iv_game_image5:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLinkArrayList.get(4).getImageLink()));
                startActivity(intent);
                break;
            case R.id.iv_game_image6:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLinkArrayList.get(5).getImageLink()));
                startActivity(intent);
                break;
        }
    }
}
