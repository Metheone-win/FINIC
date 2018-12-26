package com.wcoast.finic.model;

public class ModelSender {
    
    private String senderImg;

    private String senderName;
    private ModelChatUsers chat;

    public ModelSender(String senderImg, String senderName, ModelChatUsers chat) {
        this.senderImg = senderImg;
        this.senderName = senderName;
        this.chat = chat;

    }

    public String getSenderImg() {
        return senderImg;
    }

    public void setSenderImg(String senderImg) {
        this.senderImg = senderImg;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public ModelChatUsers getChat() {
        return chat;
    }

    public void setChat(ModelChatUsers chat) {
        this.chat = chat;
    }
}
