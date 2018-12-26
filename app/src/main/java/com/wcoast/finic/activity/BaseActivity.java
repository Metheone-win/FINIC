package com.wcoast.finic.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    protected ProgressDialog progressDialogWhite;
    protected ProgressDialog progressDialogBlue;

    public static long getDateInMillis(String srcDate) {

        long dateInMillis = 0;
        try {
            Date date = inputFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        progressDialogWhite = new ProgressDialog(this, R.style.NewDialogWhite);
        progressDialogWhite.setCancelable(false);

        progressDialogBlue = new ProgressDialog(this, R.style.NewDialogBlue);
        progressDialogBlue.setCancelable(true);
  /*  progressDialogBlue.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onBackPressed();
            }
        });

        progressDialogWhite.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onBackPressed();
            }
        });*/

        super.onCreate(savedInstanceState);
    }

    protected void showLoading(Boolean show) {

        if (show) {
            progressDialogWhite.show();
        } else progressDialogWhite.dismiss();

    }

    protected void showLoading(Boolean show, int i) {

        if (show) {
            progressDialogBlue.show();
        } else progressDialogBlue.dismiss();

    }

    public Boolean isLoggedIn() {

        if (new SessionManager(this).isUserLoggedIn()) {
            return true;
        } else
            return false;

    }

    public void setProfilePic(ImageView view) {

        HashMap<String, String> userData = new SessionManager(this).getUserDetails();

        if (!userData.get(SessionManager.KEY_PROFILE_PIC).isEmpty()) {

            Picasso.get().load(userData.get(SessionManager.KEY_PROFILE_PIC)).into(view);

        } else view.setImageResource(R.drawable.placeholder);

    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view != null) {
                assert imm != null;
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}