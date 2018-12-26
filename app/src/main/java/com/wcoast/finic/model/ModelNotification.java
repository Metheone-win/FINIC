package com.wcoast.finic.model;

public class ModelNotification {

   private String notificationDesc;
    private String notificationDate;
    private String notificationTitle;
    private String notificationId;

    public ModelNotification(String notificationDesc, String notificationDate, String notificationTitle, String notificationId) {
        this.notificationDesc = notificationDesc;
        this.notificationDate = notificationDate;
        this.notificationTitle = notificationTitle;
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public ModelNotification() {
    }

}
