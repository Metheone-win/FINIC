package com.wcoast.finic.volley;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wcoast.finic.FINIC_App;
import com.wcoast.finic.R;
import com.wcoast.finic.activity.LoginActivity;
import com.wcoast.finic.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Deepak on 07/03/2018.
 * Class to create and handle volley request.
 */

public class VolleyRequestHelper {

    private static final String TAG = VolleyRequestHelper.class.getSimpleName();
    private static int MY_SOCKET_TIMEOUT_MS = 15000;
    private static final int RETRY_TWO = 2;
    private static Intent intent = null;

    /**
     * Method to create new POST request using volley and handle response.
     *
     * @param context          Context of calling class.
     * @param jsonObject       Parameters to be passed with request in json format.
     * @param url              Url to be called.
     * @param responseListener VolleyResponseListener  to handle callback.
     * @param cacheEnable      boolean flag whether to use cache or not with the request.
     */
    public static void VolleyPostRequest(final int requestCode, final Context context, final JSONObject jsonObject, String url, final VolleyResponseListener responseListener, boolean cacheEnable) {
        Log.e(TAG, "url = " + url);
        Log.e(TAG, "param = " + jsonObject.toString());
        final JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.e(TAG, response.toString());
                responseListener.onSuccess(requestCode, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    int statusCode = error.networkResponse.statusCode;
                    switch (statusCode) {
                        case 400:
                            Log.e(TAG, "Bad request");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_one));
                            break;
                        case 401:
                            Log.e(TAG, "Unauthorized request");
                            logoutUser(context);
                            responseListener.onError(requestCode, context.getString(R.string.server_error_two));
                            break;
                        case 403:
                            Log.e(TAG, "Unauthorized request");
                            logoutUser(context);
                            responseListener.onError(requestCode, context.getString(R.string.server_error_two));
                            break;
                        case 404:
                            Log.e(TAG, "Address not found. Invalid url.");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_three));
                            break;
                        case 500:
                            Log.e(TAG, "Internal server error");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_four));
                            break;

                        case 503:
                            Log.e(TAG, "Service unavailable. Server unable to handle request.");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_four));
                            break;
                        default:
                            responseListener.onError(requestCode, error.getMessage());
                            Log.e(TAG, error.getMessage());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseListener.onError(requestCode, error.getMessage());
                    Log.e(TAG, e.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                /*
                 * if user is logged in
                 */
                if (new SessionManager(context).isUserLoggedIn()) {

                    params.put("content-type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Authorization", new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));

                    Log.d(TAG, "VolleyPostRequest: " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));
                } else {
                    params.put("content-type", "application/json");
                }
                return params;
            }
        };
        try {
            Log.e(TAG, "postrequest= " + postRequest.getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        postRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, RETRY_TWO, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        postRequest.setShouldCache(cacheEnable);
        if (cacheEnable) {
            postRequest.setCacheEntry(FINIC_App.getInstance().getDiskCache().get(url));
        }
        /*if header is required with the request.*/


        FINIC_App.getInstance().addToRequestQueue(postRequest, null);

    }

    /**
     * Method to create new GET request using volley and handle response.
     *
     * @param context          Context of calling class.
     * @param jsonObject       Parameters to be passed with request in json format.
     * @param url              Url to be called.
     * @param responseListener VolleyResponseListener  to handle callback.
     * @param cacheEnable      boolean flag whether to use cache or not with the request.
     */
    public static void VolleyGetRequest(final int requestCode, final Context context, JSONObject jsonObject, String url, final VolleyResponseListener responseListener, boolean cacheEnable) {
        Log.e(TAG, "url = " + url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                Log.e(TAG, response.toString());
                responseListener.onSuccess(requestCode, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                int statusCode = error.networkResponse.statusCode;
                switch (statusCode) {
                    case 400:
                        Log.e(TAG, "Bad request");
                        responseListener.onError(requestCode, context.getString(R.string.server_error_one));
                        break;
                    case 401:
                        Log.e(TAG, "Unauthorized request");
                        logoutUser(context);
                        responseListener.onError(requestCode, context.getString(R.string.server_error_two));
                        break;
                    case 403:
                        logoutUser(context);
                        Log.e(TAG, "Unauthorized request");
                        responseListener.onError(requestCode, context.getString(R.string.server_error_two));
                        break;
                    case 404:
                        Log.e(TAG, "Address not found. Invalid url.");
                        responseListener.onError(requestCode, context.getString(R.string.server_error_three));
                        break;
                    case 500:
                        Log.e(TAG, "Internal server error");
                        responseListener.onError(requestCode, context.getString(R.string.server_error_four));
                        break;

                    case 503:
                        Log.e(TAG, "Service unavailable. Server unable to handle request.");
                        responseListener.onError(requestCode, context.getString(R.string.server_error_four));
                        break;
                    default:
                        responseListener.onError(requestCode, error.getMessage());
                        break;
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                /*
                 * if user is logged in
                 */
                if (new SessionManager(context).isUserLoggedIn()) {

                    params.put("content-type", "application/json");
                    params.put("Accept", "application/json");
                    params.put("Authorization", new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));

                    Log.d(TAG, "VolleyPostRequest: " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));
                } else {
                    params.put("content-type", "application/json");
                }
                return params;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, RETRY_TWO, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequest.setShouldCache(cacheEnable);
        if (cacheEnable) {
            getRequest.setCacheEntry(FINIC_App.getInstance().getDiskCache().get(url));
        }

        /*if header is required with the request.*/
        /*try {
            postRequest.getHeaders().put("", "");
        } catch (AuthFailureError authFailureError) {
            responseListener.onError(requestCode,context.getString(R.string.server_error_two));
        }*/

        FINIC_App.getInstance().addToRequestQueue(getRequest, null);
    }

    public static void VolleyMultiPartRequest(final int requestCode, final Context context, final ArrayList<MultiPartData> multiPartDataList, String url, final VolleyResponseListener responseListener, boolean cacheEnable) {
        Log.d(TAG, "url = " + url);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject jsonObject = new JSONObject(resultResponse);
                    // parse success output
                    Log.d(TAG, "response = " + response.toString());
                    Log.d(TAG, "resultResponse = " + resultResponse);
                    responseListener.onSuccess(requestCode, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseListener.onError(requestCode, resultResponse);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    int statusCode = error.networkResponse.statusCode;
                    switch (statusCode) {
                        case 400:
                            Log.e(TAG, "Bad request");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_one));
                            break;
                        case 401:
                            Log.e(TAG, "Unauthorized request");
                            logoutUser(context);
                            responseListener.onError(requestCode, context.getString(R.string.server_error_two));
                            break;
                        case 403:
                            Log.e(TAG, "Unauthorized request");
                            logoutUser(context);
                            responseListener.onError(requestCode, context.getString(R.string.server_error_two));
                            break;
                        case 404:
                            Log.e(TAG, "Address not found. Invalid url.");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_three));
                            break;
                        case 500:
                            Log.e(TAG, "Internal server error");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_four));
                            break;

                        case 503:
                            Log.e(TAG, "Service unavailable. Server unable to handle request.");
                            responseListener.onError(requestCode, context.getString(R.string.server_error_four));
                            break;

                        default:
                            responseListener.onError(requestCode, error.getMessage());
                            break;
                    }
                } catch (Exception e) {
                    responseListener.onError(requestCode, error.getMessage());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                for (MultiPartData multiPartData : multiPartDataList) {
                    if (!multiPartData.getIsFile()) {
                        params.put(multiPartData.getParamName(), multiPartData.getParamValue());
                        Log.d(TAG, "getParams: " + params);
                    }
                }
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (MultiPartData multiPartData : multiPartDataList) {
                    if (multiPartData.getIsFile()) {
                        File file = new File(multiPartData.getParamValue());
                        byte[] bytes = getBytesFromFile(multiPartData.getParamValue());
                        if (bytes != null)
                            params.put(multiPartData.getParamName(), new DataPart(file.getName(), bytes, multiPartData.getMimeType()));
                        Log.d(TAG, "getByteData: " + params);
                    }
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("content-type", "multipart/form-data");
                if (new SessionManager(context).isUserLoggedIn()) {
                    Log.d(TAG, "LoggedIN");
                    params.put("Authorization", new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));
                    Log.d(TAG, "VolleyPostRequest: " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));
                }
                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, RETRY_TWO, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        multipartRequest.setShouldCache(cacheEnable);
        if (cacheEnable) {
            multipartRequest.setCacheEntry(FINIC_App.getInstance().getDiskCache().get(url));
        }
        /*if header is required with the request.*/
      /*  try {
            multipartRequest.getHeaders().put("content-type", "application/json");
            multipartRequest.getHeaders().put("Accept", "application/json");
            multipartRequest.getHeaders().put("Authorization", new SessionManager(context).getUserDetails().get(SessionManager.KEY_TOKEN_TYPE) + " " + new SessionManager(context).getUserDetails().get(SessionManager.KEY_AUTH));
        } catch (Exception e) {
            responseListener.onError(requestCode, context.getString(R.string.server_error_two));
            Log.e(TAG, "VolleyPostRequest: ", e);
        }
        try {
            Log.e(TAG, "postrequest= " + multipartRequest.getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }*/
        FINIC_App.getInstance().addToRequestQueue(multipartRequest, null);
    }

    private static byte[] getBytesFromFile(String path) {
        File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);


            byte buffer[] = new byte[(int) ((file.length() + 100))];
            int read = 0;

            while (read != -1) {
                // Do what you want with the buffer of bytes here.
                // Make sure you only work with bytes 0 - read.
                // Sending it with your protocol for example.
                read = fis.read(buffer);
            }
            return buffer;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.toString());
        } catch (IOException e) {
            System.out.println("Exception reading file: " + e.toString());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    public static void logoutUser(Context c) {
        Intent intent = null;
        new SessionManager(c).logoutUser();
        Toast.makeText(c, "Please login again", Toast.LENGTH_SHORT).show();
        intent = new Intent(c, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent);
    }
}
