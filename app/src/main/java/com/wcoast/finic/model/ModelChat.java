package com.wcoast.finic.model;

import java.util.Date;

public class ModelChat {
    private String senderID;
    private String message;
    private String messageTime;
    private String receiverID;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ModelChat() {
    }

    public ModelChat(String SenderID, String Message) {
        this.senderID = SenderID;
        this.message = Message;
        messageTime = new Date().getTime() + "";
        receiverID = "9";
        status= "Unread";
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

}
