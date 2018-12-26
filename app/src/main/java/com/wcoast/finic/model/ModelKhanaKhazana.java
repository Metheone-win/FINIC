package com.wcoast.finic.model;

import java.util.ArrayList;

public class ModelKhanaKhazana {

    private int subCategoryId;
    private String subCategoryName;
    private ArrayList<String> imageNames;
    private String iconNames;

    public String getIconNames() {
        return iconNames;
    }

    public void setIconNames(String iconNames) {
        this.iconNames = iconNames;
    }

    public ModelKhanaKhazana(int subCategoryId, String subCategoryName, ArrayList<String> imageNames, String iconNames) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.imageNames = imageNames;
        this.iconNames = iconNames;

    }

    public ModelKhanaKhazana() {
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public ArrayList<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(ArrayList<String> imageNames) {
        this.imageNames = imageNames;
    }
}
