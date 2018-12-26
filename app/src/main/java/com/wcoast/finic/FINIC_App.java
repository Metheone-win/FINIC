package com.wcoast.finic;

import android.app.Application;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;

public class FINIC_App extends Application {
    public static FINIC_App mInstance;
    private RequestQueue mRequestQueue;
    private DiskBasedCache cache;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized FINIC_App getInstance() {
        return mInstance;
    }


    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }


    public DiskBasedCache getDiskCache() {
        if (cache == null) {
            int MAX_VOLLEY_CACHE = 209715200;
            cache = new DiskBasedCache(getExternalCacheDir(), MAX_VOLLEY_CACHE);
        }
        return cache;
    }
}
