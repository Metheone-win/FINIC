package com.wcoast.finic.volley;

/*
  Created by abc on 07/03/2018.
 */

import org.json.JSONObject;

/**
 * Interface to create callback method for volley response.
 */

public interface VolleyResponseListener {

    /**
     * Method will be called when request made and returned response successfully.
     */
    void onSuccess(int requestCode,JSONObject json);

    /**
     * Method will be called when error occurred during  volley request.
     */
    void onError(int requestCode,String message);

    /**
     * Method will be called when volley request is canceled due to some reason.
     */
    void onCanceled(int requestCode,String message);


}
