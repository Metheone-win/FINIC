package com.wcoast.finic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelLevelDetails implements Parcelable {
    @SerializedName("level")
    private String level;

    @SerializedName("level_amount")
    private String levelAmount;

    @SerializedName("point_total")
    private String pointTotal;

    @SerializedName("child_total")
    private String childTotal;

    @SerializedName("point_added")
    private String pointAdded;

    @SerializedName("child_added")
    private String childAdded;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelAmount() {
        return levelAmount;
    }

    public void setLevelAmount(String levelAmount) {
        this.levelAmount = levelAmount;
    }

    public String getPointTotal() {
        return pointTotal;
    }

    public void setPointTotal(String pointTotal) {
        this.pointTotal = pointTotal;
    }

    public String getChildTotal() {
        return childTotal;
    }

    public void setChildTotal(String childTotal) {
        this.childTotal = childTotal;
    }

    public String getPointAdded() {
        return pointAdded;
    }

    public void setPointAdded(String pointAdded) {
        this.pointAdded = pointAdded;
    }

    public String getChildAdded() {
        return childAdded;
    }

    public void setChildAdded(String childAdded) {
        this.childAdded = childAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.level);
        dest.writeString(this.levelAmount);
        dest.writeString(this.pointTotal);
        dest.writeString(this.childTotal);
        dest.writeString(this.pointAdded);
        dest.writeString(this.childAdded);
    }

    public ModelLevelDetails() {
    }

    protected ModelLevelDetails(Parcel in) {
        this.level = in.readString();
        this.levelAmount = in.readString();
        this.pointTotal = in.readString();
        this.childTotal = in.readString();
        this.pointAdded = in.readString();
        this.childAdded = in.readString();
    }

    public static final Parcelable.Creator<ModelLevelDetails> CREATOR = new Parcelable.Creator<ModelLevelDetails>() {
        @Override
        public ModelLevelDetails createFromParcel(Parcel source) {
            return new ModelLevelDetails(source);
        }

        @Override
        public ModelLevelDetails[] newArray(int size) {
            return new ModelLevelDetails[size];
        }
    };
}
