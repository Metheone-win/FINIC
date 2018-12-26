package com.wcoast.finic.model;

public class ModelUser {
    private String secondUserImage;

    private String secondUserName;

    public String getSecondUserToken() {
        return secondUserToken;
    }

    public void setSecondUserToken(String secondUserToken) {
        this.secondUserToken = secondUserToken;
    }

    private String secondUserToken;

    public int getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(int secondUserId) {
        this.secondUserId = secondUserId;
    }

    private int secondUserId;

    private ModelChatUsers chat;

    public ModelUser(String secondUserImage, String secondUserName, String secondUserToken, ModelChatUsers chat, int secondUserId) {
        this.secondUserImage = secondUserImage;
        this.secondUserName = secondUserName;
        this.secondUserToken = secondUserToken;
        this.secondUserId = secondUserId;
        this.chat = chat;
    }

    public ModelUser() {
    }

    public String getSecondUserImage() {
        return secondUserImage;
    }

    public void setSecondUserImage(String secondUserImage) {
        this.secondUserImage = secondUserImage;
    }

    public String getSecondUserName() {
        return secondUserName;
    }

    public void setSecondUserName(String secondUserName) {
        this.secondUserName = secondUserName;
    }

    public ModelChatUsers getChat() {
        return chat;
    }

    public void setChat(ModelChatUsers chat) {
        this.chat = chat;
    }
}
