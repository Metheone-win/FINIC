package com.wcoast.finic.model;

public class ModelCategory
{
  private  int catId;
    private  String catName;
    private int catImage;

    private String catInformationText;

    public String getCatInformationText() {
        return catInformationText;
    }

    public void setCatInformationText(String catInformationText) {
        this.catInformationText = catInformationText;
    }

    public int getCatId()
    {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatImage() {
        return catImage;
    }

    public void setCatImage(int catImage) {
        this.catImage = catImage;
    }



    public ModelCategory() {
    }



}
