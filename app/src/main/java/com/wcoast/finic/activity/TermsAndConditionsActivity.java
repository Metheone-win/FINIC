package com.wcoast.finic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.webkit.WebView;
import android.widget.TextView;

import com.wcoast.finic.R;

import java.io.InputStream;
import java.nio.Buffer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAndConditionsActivity extends AppCompatActivity {
    @BindView(R.id.wv_terms_condition)
    WebView wv_terms_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
        wv_terms_condition.loadUrl("file:///android_asset/terms_and_condition.html");
    }
}
