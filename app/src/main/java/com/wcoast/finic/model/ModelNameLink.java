package com.wcoast.finic.model;

public class ModelNameLink {

    private String gameImage;
    private String gameLink;

    public String getImageLink() {
        return gameLink;
    }

    public void setGameLink(String gameLink) {
        this.gameLink = gameLink;
    }

    public ModelNameLink(String gameImage, String gameLink) {
        this.gameImage = gameImage;

        this.gameLink = gameLink;

    }

    public ModelNameLink() {
    }

    public String getImageName() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
    }

}
