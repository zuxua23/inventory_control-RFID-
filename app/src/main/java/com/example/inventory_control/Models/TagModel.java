package com.example.inventory_control.Models;

public class TagModel {
    private String epcTag;
    private String productName;

    public TagModel(String epcTag, String productName) {
        this.epcTag = epcTag;
        this.productName = productName;
    }

    public String getEpcTag() { return epcTag; }
    public String getProductName() { return productName; }
}