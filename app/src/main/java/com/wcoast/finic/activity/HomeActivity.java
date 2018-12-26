package com.wcoast.finic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.adapter.HomeCategoryAdapter;
import com.wcoast.finic.model.ModelCategory;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.tv_game_name_ps2)
    TextView tvGamePs2;

    @BindView(R.id.tv_game_name_xbox)
    TextView tvGameXBOX;

    @BindView(R.id.tv_game_name_kineet)
    TextView tvGameKineet;

    @BindView(R.id.cv_second_banner)
    CardView cvBanner2;

    @BindView(R.id.cv_forth_banner)
    CardView cvBanner3;

    @BindView(R.id.cv_guidance)
    CardView cvGuidance;

    @BindView(R.id.cv_about_us)
    CardView cvAboutUs;

    @BindView(R.id.cv_login)
    CardView cvLogin;

    @BindView(R.id.cv_online_services)
    CardView cvOnlineServices;

    @BindView(R.id.cv_khana_khazana)
    CardView cvKhanaKhazana;

    @BindView(R.id.cv_sign_up)
    CardView cvSignUp;

    @BindView(R.id.tv_login_text)
    TextView tvLoginText;

    @BindView(R.id.iv_login)
    ImageView ivLogin;

    private TextView tvMobile, tvName;
    private ImageView profilePic;

    private List<ModelCategory> categoriesList = new ArrayList<>();

    private NavigationView navigationView;
    private ImageButton btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);


        btnEditProfile = hView.findViewById(R.id.btn_edit_profile);
        tvMobile = hView.findViewById(R.id.tv_mobile);
        tvName = hView.findViewById(R.id.tv_name);
        profilePic = hView.findViewById(R.id.iv_profile_image);

        Log.e("auth = ",new SessionManager(this).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(this).getUserDetails().get(SessionManager.KEY_AUTH));
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UpdateProfileAct.class);
                startActivity(intent);
            }
        });

        HomeCategoryAdapter homeCategoryAdapter = new HomeCategoryAdapter(this, categoriesList);
        if (new SessionManager(this).isUserLoggedIn()) {
            cvSignUp.setVisibility(View.INVISIBLE);
            tvLoginText.setText("Logout");
            ivLogin.setImageResource(R.drawable.ic_logout);
        }

        tvGameKineet.setOnClickListener(this);
        tvGamePs2.setOnClickListener(this);
        tvGameXBOX.setOnClickListener(this);

        cvBanner2.setOnClickListener(this);
        cvBanner3.setOnClickListener(this);
        cvSignUp.setOnClickListener(this);
        cvKhanaKhazana.setOnClickListener(this);
        cvOnlineServices.setOnClickListener(this);
        cvLogin.setOnClickListener(this);
        cvAboutUs.setOnClickListener(this);
        cvGuidance.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {

            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
            // Snackbar.make(tvMobile, "ABOUT US URL - Coming Soon!", Snackbar.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {

            new SessionManager(getBaseContext()).logoutUser();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            //   intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        } else if (id == R.id.nav_login) {

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_noti) {

            if (isLoggedIn()) {
                Intent intent = new Intent(HomeActivity.this, NotificationAct.class);
                startActivity(intent);
            } else
                showSnackbar();

        } else if (id == R.id.nav_chats) {

            if (isLoggedIn()) {
                Intent intent = new Intent(HomeActivity.this, AllChats.class);
                startActivity(intent);
            } else
                showSnackbar();

        } else if (id == R.id.nav_refer) {

            if (isLoggedIn()) {
                Intent intent = new Intent(HomeActivity.this, ReferActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } else
                showSnackbar();

        } else if (id == R.id.nav_working) {
            Intent intent = new Intent(HomeActivity.this, HowItWorksAct.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void addDataCategories(int catId, String catName, int catImg, String catInfo) {

        ModelCategory modelCategory = new ModelCategory();

        modelCategory.setCatName(catName);
        modelCategory.setCatId(catId);
        modelCategory.setCatImage(catImg);
        modelCategory.setCatInformationText(catInfo);

        categoriesList.add(modelCategory);

    }

    private void hideItem(int id) {

        navigationView = findViewById(R.id.nav_view);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(false);
        nav_Menu.findItem(id).setEnabled(false);

    }

    private void showItem(int id) {

        navigationView = findViewById(R.id.nav_view);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(id).setVisible(true);
        nav_Menu.findItem(id).setEnabled(true);

    }

    private void showSnackbar() {

        Snackbar.make(tvMobile, getResources().getString(R.string.activate_your_account), Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isLoggedIn()) {

            HashMap<String, String> userData = new SessionManager(this).getUserDetails();

            hideItem(R.id.nav_login);
            showItem(R.id.nav_logout);

            tvName.setText(userData.get(SessionManager.KEY_NAME));
            tvMobile.setText(userData.get(SessionManager.KEY_MOBILE_NO));

            cvSignUp.setVisibility(View.INVISIBLE);
            tvLoginText.setText("Logout");
            ivLogin.setImageResource(R.drawable.ic_logout);

            setProfilePic(profilePic);

        } else {

            btnEditProfile.setVisibility(View.INVISIBLE);
            btnEditProfile.setClickable(false);

            hideItem(R.id.nav_logout);
            showItem(R.id.nav_login);

            tvName.setText(R.string.guest);
            tvMobile.setText(R.string.welcome);

            cvSignUp.setVisibility(View.VISIBLE);
            tvLoginText.setText("Login");
            ivLogin.setImageResource(R.drawable.ic_login);

            profilePic.setImageResource(R.drawable.placeholder);
        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.cv_about_us:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);

                break;
            case R.id.cv_guidance:

                intent = new Intent(this, Guidance.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.tv_game_name_ps2:

                intent = new Intent(this, GamesActivity.class);
                intent.putExtra(Constant.GAME_PS2, 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;

            case R.id.tv_game_name_xbox:

                intent = new Intent(this, GamesActivity.class);
                intent.putExtra(Constant.GAME_XBOX, 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;

            case R.id.tv_game_name_kineet:

                intent = new Intent(this, GamesActivity.class);
                intent.putExtra(Constant.GAME_KINECT, 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;

            case R.id.cv_online_services:

                intent = new Intent(this, OnlineServices.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                break;

            case R.id.cv_forth_banner:

                if (new SessionManager(this).isUserLoggedIn()) {
                    intent = new Intent(this, ReferActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ReferAndEarnDescription.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }

                break;
            case R.id.cv_login:

                if (new SessionManager(this).isUserLoggedIn()) {

                    new SessionManager(this).logoutUser();

                    intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                } else {
                    intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }

                break;
            case R.id.cv_sign_up:

                intent = new Intent(this, SignupActivity.class);
                startActivity(intent);

                break;
            case R.id.cv_khana_khazana:
                intent = new Intent(this, KhanaKhazanaSubCategory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

        }

    }
}
