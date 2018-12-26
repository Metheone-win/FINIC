package com.wcoast.finic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ModelChatUsers implements Parcelable {
    @SerializedName("senderID")
    private int senderID;

    @SerializedName("message")
    private String message;

    @SerializedName("messageTime")
    private long messageTime;

    @SerializedName("receiverID")
    private int receiverID;

    public ModelChatUsers() {
    }

    public ModelChatUsers(int senderID, String message, int receiverID) {
        this.senderID = senderID;
        messageTime = new Date().getTime();
        this.message = message;
        this.receiverID = receiverID;
    }


    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.senderID);
        dest.writeString(this.message);
        dest.writeLong(this.messageTime);
        dest.writeInt(this.receiverID);
    }

    protected ModelChatUsers(Parcel in) {
        this.senderID = in.readInt();
        this.message = in.readString();
        this.messageTime = in.readLong();
        this.receiverID = in.readInt();
    }

    public static final Parcelable.Creator<ModelChatUsers> CREATOR = new Parcelable.Creator<ModelChatUsers>() {
        @Override
        public ModelChatUsers createFromParcel(Parcel source) {
            return new ModelChatUsers(source);
        }

        @Override
        public ModelChatUsers[] newArray(int size) {
            return new ModelChatUsers[size];
        }
    };
}