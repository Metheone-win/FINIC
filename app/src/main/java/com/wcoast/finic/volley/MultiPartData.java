package com.wcoast.finic.volley;

import android.support.annotation.NonNull;

/**
 * Created by abc on 23/03/2018.
 */

public class MultiPartData {

    private String paramName;
    private String paramValue;
    private boolean isFile;
    private String mimeType;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(@NonNull String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(@NonNull String paramValue) {
        this.paramValue = paramValue;
    }

    public boolean getIsFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(@NonNull String mimeType) {
        this.mimeType = mimeType;
    }
}
