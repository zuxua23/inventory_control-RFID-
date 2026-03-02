package com.example.inventory_control.Models;

public class ItemModel {
    private String epcTag;
    private String itemId;
    private String itemName;
    private int qty;

    public ItemModel(String epcTag, String itemId, String itemName, int qty) {
        this.epcTag = epcTag;
        this.itemId = itemId;
        this.itemName = itemName;
        this.qty = qty;
    }

    public String getEpcTag() { return epcTag; }
    public String getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public int getQty() { return qty; }

    public void setQty(int qty) { this.qty = qty; }
}