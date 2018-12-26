package com.wcoast.finic.model;

public class ModelNameImageLink {
    private String name;
    private String imageName;
    private String link;

    public ModelNameImageLink(String name, String imageName, String link) {
        this.name = name;
        this.imageName = imageName;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public String getLink() {
        return link;
    }
}
