package com.wcoast.finic.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wcoast.finic.R;
import com.wcoast.finic.utility.ConnectionUtil;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.FileUtil;
import com.wcoast.finic.utility.SessionManager;
import com.wcoast.finic.utility.Validation;
import com.wcoast.finic.volley.MultiPartData;
import com.wcoast.finic.volley.VolleyHelper;
import com.wcoast.finic.volley.VolleyRequestHelper;
import com.wcoast.finic.volley.VolleyResponseListener;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

public class UpdateProfileAct extends BaseActivity implements VolleyResponseListener, View.OnClickListener {
    private static final String TAG = "UpdateProfileAct";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_active_status)
    Button btnActiveStatus;
    @BindView(R.id.et_full_name)
    TextInputLayout etFullName;
    @BindView(R.id.til_mobile_no)
    TextInputLayout etMobile;
    @BindView(R.id.til_email)
    TextInputLayout etEmail;
    @BindView(R.id.et_address)
    TextInputLayout etAddress;
    @BindView(R.id.btn_update_profile)
    Button btnUpdateProfile;

    @BindView(R.id.btn_edit_image)
    ImageView btnEditImage;

    @BindView(R.id.iv_image_profile)
    ImageView imgProfilePic;

    @BindView(R.id.et_get_address)
    EditText etOpenPlacePicker;
    String[] requestPermissions = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
    };
    private String email, name, address;
    private int REQUEST_CODE = 121;
    private int PLACE_PICKER_REQUEST = 123;

    private String longitude, latitude;

    private File compressedImage;
    private File actualImage;

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pro);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        HashMap<String, String> userData = new SessionManager(this).getUserDetails();

        etMobile.getEditText().setText(userData.get(SessionManager.KEY_MOBILE_NO));
        etFullName.getEditText().setText(userData.get(SessionManager.KEY_NAME));
        etAddress.getEditText().setText(!userData.get(SessionManager.KEY_ADDRESS).isEmpty() ? userData.get(SessionManager.KEY_ADDRESS) : "");

        etFullName.getEditText().setSelection(etFullName.getEditText().getText().length());

        etEmail.getEditText().setText(userData.get(SessionManager.KEY_EMAIL));
        etMobile.setEnabled(false);

        setProfilePic(imgProfilePic);

        btnUpdateProfile.setOnClickListener(this);
        btnEditImage.setOnClickListener(this);
        etOpenPlacePicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_profile:

                getValues();
                maintainError();
                Boolean isCancel = false;
                View focusView = null;

                if (address.isEmpty()) {

                    isCancel = true;
                    focusView = etAddress;
                    etAddress.setError(getResources().getString(R.string.error_empty));

                } else if (email.isEmpty()) {

                    isCancel = true;
                    focusView = etEmail;
                    etEmail.setError(getResources().getString(R.string.error_empty));

                } else if (!Validation.isValidEmail(email)) {

                    isCancel = true;
                    focusView = etEmail;
                    etEmail.setError(getResources().getString(R.string.error_email));

                } else if (name.isEmpty()) {
                    isCancel = true;
                    focusView = etFullName;
                    etFullName.setError(getResources().getString(R.string.error_empty));
                }
                if (isCancel) {
                    if (focusView != null) {
                        focusView.requestFocus();
                    }
                } else {
                    if (ConnectionUtil.isInternetOn(getBaseContext())) {

                        showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);

                        String name = etFullName.getEditText().getText().toString().trim();
                        String email = etEmail.getEditText().getText().toString().trim();

                        VolleyRequestHelper.VolleyPostRequest(VolleyHelper.REQ_CODE_UPDATE_PROFILE, getBaseContext(), new VolleyHelper().getUpdateProfileParams(name, email, address, latitude, longitude), VolleyHelper.UPDATE_PROFILE_URL, UpdateProfileAct.this, false);

                    } else {
                        Snackbar.make(etMobile, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                    }
                }
                break;


            case R.id.btn_edit_image:

                requestPermission();

                break;

            case R.id.et_get_address:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(UpdateProfileAct.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // Image Setup
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri imageUri = result.getUri();

                if (ConnectionUtil.isInternetOn(getBaseContext())) {

                    ((ImageView) findViewById(R.id.iv_image_profile)).setImageURI(imageUri);

                    try {
                        actualImage = FileUtil.from(this, imageUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String imagePath = compressImage();

                    Log.d(TAG, "onActivityResult() returned: " + imagePath);

                    uploadImageToServer(imagePath);

                } else {

                    Snackbar.make(imgProfilePic, getString(R.string.string_no_connection), Snackbar.LENGTH_LONG).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                String placeStr = place.getAddress() + "";
                String latLong = String.valueOf(place.getLatLng()).substring(10, String.valueOf(place.getLatLng()).length() - 1);

                String[] divideLatLong = latLong.split(",");
                latitude = divideLatLong[0];
                longitude = divideLatLong[1];
                address = place.getAddress() + "";

                etAddress.getEditText().setText(placeStr);
            }
        }
    }

    //upload image to server
    private void uploadImageToServer(String imagePath) {

        ArrayList<MultiPartData> multiPartDataArrayList = new ArrayList<>();

        MultiPartData profileImage = new MultiPartData();
        profileImage.setIsFile(true);
        profileImage.setMimeType("image/*");
        profileImage.setParamName(Constant.PROFILE_PIC);
        profileImage.setParamValue(imagePath);

        Log.d(TAG, "uploadImageToServer: " + imagePath);
        multiPartDataArrayList.add(profileImage);

        VolleyRequestHelper.VolleyMultiPartRequest(VolleyHelper.REQ_CODE_CHANGE_PROFILE_IMAGE, this, multiPartDataArrayList, VolleyHelper.UPDATE_PROFILE_IMAGE_URL, UpdateProfileAct.this, false);

    }

    //Volley Response Classes
    @Override
    public void onSuccess(int requestCode, JSONObject json) {

        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

        switch (requestCode) {

            case VolleyHelper.REQ_CODE_CHANGE_PROFILE_IMAGE:

                Boolean status = json.optBoolean(Constant.STATUS);
                String message = json.optString(Constant.MESSAGE);

                if (status) {

                    JSONObject response = json.optJSONObject(Constant.RESPONSE);
                    JSONObject userData = response.optJSONObject(Constant.USER_DATA);
                    Snackbar.make(etAddress, message, Snackbar.LENGTH_SHORT).show();

                    new SessionManager(getBaseContext()).saveImagePath(userData.optString(SessionManager.KEY_PROFILE_PIC));

                    //  setProfilePic(imgProfilePic);

                } else setProfilePic(imgProfilePic);

                break;

            case VolleyHelper.REQ_CODE_UPDATE_PROFILE:

                Boolean status1 = json.optBoolean(Constant.STATUS);
                String message1 = json.optString(Constant.MESSAGE);

                if (status1) {

                    Snackbar.make(etAddress, message1, Snackbar.LENGTH_SHORT).show();

                    new SessionManager(getBaseContext()).saveUpdatedUser(etFullName.getEditText().getText().toString().trim(), etEmail.getEditText().getText().toString().trim(), etAddress.getEditText().getText().toString());

                }
                break;
        }
    }

    @Override
    public void onError(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        Snackbar.make(imgProfilePic, message, Snackbar.LENGTH_LONG).show();
        Log.e(TAG, "onError: " + message);
    }

    @Override
    public void onCanceled(int requestCode, String message) {
        showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
        Snackbar.make(imgProfilePic, message, Snackbar.LENGTH_LONG).show();
        Log.e(TAG, "onCanceled: " + message);
    }

    private void getValues() {
        email = etEmail.getEditText().getText().toString().trim();
        name = etFullName.getEditText().getText().toString().trim();
        address = etAddress.getEditText().getText().toString().trim();
    }


//Requesting Run-Time permissions.

    private void maintainError() {
        etFullName.setError(null);
        etMobile.setError(null);
        etEmail.setError(null);
        etAddress.setError(null);
        etFullName.setErrorEnabled(false);
        etMobile.setErrorEnabled(false);
        etEmail.setErrorEnabled(false);
        etAddress.setErrorEnabled(false);
    }

    private void requestPermission() {

        if (!hasPermissions(this, requestPermissions)) {

            ActivityCompat.requestPermissions(this, requestPermissions, REQUEST_CODE);

        } else

            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(5, 3).start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(25, 12).start(this);

            } else {
                Toast.makeText(this, "You must allow permission to select file from your device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String compressImage() {

        if (actualImage != null) {

            try {
                compressedImage = new Compressor(this)
                        .setMaxWidth(512)
                        .setMaxHeight(206)
                        .setQuality(80)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(actualImage);

                Log.e(TAG, "compressed " + compressedImage.getPath());

            } catch (IOException e) {
                e.printStackTrace();

            }

            return compressedImage.getPath() + "";
        }
        return "";
    }
}
