package com.wcoast.finic.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ReferActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /* @BindView(R.id.img_user_image)
       ImageView ivUserImage;
       @BindView(R.id.rv_referrals)
       RecyclerView rvReferrals;
     */
    @BindView(R.id.cv_amount_description)
    CardView cvAmountDescription;
    @BindView(R.id.cv_reference)
    CardView cvReference;
    @BindView(R.id.btn_share)
    Button btnShare;
    @BindView(R.id.img_copy_icon)
    ImageView btnCopy;
    @BindView(R.id.tv_referral_code)
    TextView tvReferralCode;

    /*@BindView(R.id.tv_name_initials)
     TextView tvNameInitials;
     @BindView(R.id.tv_empty)
     TextView tvEmpty;
     @BindView(R.id.layout_referal)
     LinearLayout layoutReferal;*/

    @BindView(R.id.btn_wallet)
    Button btnWallet;

    private HashMap<String, String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userData = new SessionManager(this).getUserDetails();
        tvReferralCode.setText(userData.get(Constant.REFER_CODE));

        SpannableString ss1 = new SpannableString("Points\n" + new SessionManager(this).getUserDetails().get(Constant.WALLET_AMOUNT));
        ss1.setSpan(new RelativeSizeSpan(1.05f), 0, 7, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, 7, 0);// set color

        btnWallet.setText(ss1);

        btnCopy.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnWallet.setOnClickListener(this);
        cvAmountDescription.setOnClickListener(this);
        cvReference.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_copy_icon:
                Snackbar.make(tvReferralCode, "Copied to Clipboard", Snackbar.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("FINIC", tvReferralCode.getText());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                break;

            case R.id.btn_share:
                createDynamicLink();
                break;

            case R.id.btn_wallet:
                Intent intent = new Intent(ReferActivity.this, RedeemWalletAct.class);
                startActivity(intent);
                break;

            case R.id.cv_reference:
                intent = new Intent(ReferActivity.this, ReferActivitySubTree.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;

            case R.id.cv_amount_description:
                intent = new Intent(ReferActivity.this, ReferLevelDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
        }
    }

    private void createDynamicLink() {
        //Create dynamic long
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.westcoastinfotech.com/?referby=" + userData.get(SessionManager.KEY_REFER_CODE)))
                .setDynamicLinkDomain("finic.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLinkUri)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String mInvitationUrl = shortLink.toString();
                            /*String msg = "Download and Sign Up on FINIC with my referral code or this link and earn real money every day. "
                                    + mInvitationUrl;*/
                            String msg = String.format("%s %s %s %s", getString(R.string.earn_first_msg), userData.get(Constant.REFER_CODE), getString(R.string.earn_second_msg), mInvitationUrl);
                            String subject = "Earn with FINIC with Refer Code: " + userData.get(Constant.REFER_CODE);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, msg);
                            intent.setType("text/plain");
                            startActivity(intent);
                            Log.e(TAG, "Link = " + msg);

                        } else {
                            Snackbar.make(tvReferralCode, "Something went wrong. Try again later!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
                Log.e(TAG, e.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
