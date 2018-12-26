package com.wcoast.finic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wcoast.finic.utility.Constant;

public class ModelRedeemHistory implements Parcelable {
    @SerializedName(Constant.REDEEM_DATE)
    private String date;

    @SerializedName(Constant.MESSAGE)
    private String summary;

    @SerializedName(Constant.ID)
    private String txnId;

    @SerializedName(Constant.REQUEST_POINT)
    private String txnAmount;

    @SerializedName(Constant.STATUS)
    private String txnStatus;

    public String getTxnAmount() {
        return txnAmount;
    }

    public String getTxnStatus() {
        return txnStatus;
    }


    public String getDate() {
        return date;
    }

    public String getSummary() {
        return summary;
    }

    public String getTxnId() {
        return txnId;
    }

    public ModelRedeemHistory() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.summary);
        dest.writeString(this.txnId);
        dest.writeString(this.txnAmount);
        dest.writeString(this.txnStatus);
    }

    protected ModelRedeemHistory(Parcel in) {
        this.date = in.readString();
        this.summary = in.readString();
        this.txnId = in.readString();
        this.txnAmount = in.readString();
        this.txnStatus = in.readString();
    }

    public static final Creator<ModelRedeemHistory> CREATOR = new Creator<ModelRedeemHistory>() {
        @Override
        public ModelRedeemHistory createFromParcel(Parcel source) {
            return new ModelRedeemHistory(source);
        }

        @Override
        public ModelRedeemHistory[] newArray(int size) {
            return new ModelRedeemHistory[size];
        }
    };
}
