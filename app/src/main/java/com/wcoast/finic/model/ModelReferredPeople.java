package com.wcoast.finic.model;

public class ModelReferredPeople {

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    private String userId;
    private String name;
    private String mobile;
    private String email;

    public String getReferCode() {
        return referCode;
    }

    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }

    private String referCode;

    public String getReceiverToken() {
        return receiverToken;
    }

    public void setReceiverToken(String receiverToken) {
        this.receiverToken = receiverToken;
    }

    private String receiverToken;

    public String getChildAdded() {
        return childAdded;
    }

    public void setChildAdded(String childAdded) {
        this.childAdded = childAdded;
    }

    public String getChildTotal() {
        return childTotal;
    }

    public void setChildTotal(String childTotal) {
        this.childTotal = childTotal;
    }

    private String childAdded;
    private String childTotal;

    public String getProfilePic() {
        return profilePic;
    }

    private String profilePic;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public ModelReferredPeople(String userId, String name, String mobile, String email, String date, String profilePic, String childAdded, String childTotal, String receiverToken, String referCode) {
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.profilePic = profilePic;
        this.childAdded = childAdded;
        this.childTotal = childTotal;
        this.email = email;
        this.date = date;
        this.date = date;
        this.receiverToken = receiverToken;
        this.referCode = referCode;
    }

}
