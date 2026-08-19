/*
NAME: Karen Garcia
CLASS: INFO 1531/ SS26
ASSIGNMENT: Assignment 7 - Inventory Management App
DATE: 08/10/2026
RESOURCES: For this assignment I used the videos for this module, the book, w3schools.org, and some AI to help with debugging.

This is a inventory management website for users to search through products and on the administration
      side employees can track, lookup, and manage the inventory

*/

package com.example.kgarciaassignment7;

import java.io.Serializable;

public class Item implements Serializable {
    private int id;
    private String name;
    private String manufacturer;
    private double price;
    private int inventory;
    private ItemTitle type;
    private Image image;

    public Item(){
    }

    public Item(int id, String name, String manufacturer, double price, int inventory, ItemTitle itemTitle){
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.inventory = inventory;
        this.type = itemTitle;
        image = new Image();
    }

    public Image getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getInventory() {
        return inventory;
    }

    public ItemTitle getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setType(ItemTitle type) {
        this.type = type;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean hasImage() {
        return image != null && image.getName().length() > 0 && image.getContents().length > 0;
    }
}
