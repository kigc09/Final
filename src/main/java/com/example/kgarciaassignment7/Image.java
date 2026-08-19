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

import java.util.Base64;

public class Image {
    private String id;
    private String name;
    private byte[] contents;
    private String base64Image;
    private String encoding;

    public Image() {
        name = "";
        id = "";
        contents = new byte[0];
        base64Image = "";
        encoding = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        encoding = name.substring(name.lastIndexOf('.') + 1);
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
        base64Image = Base64.getEncoder().encodeToString(contents);
    }

    public String getBase64Image() {
        return base64Image;
    }

    public String getEncoding(){
        return encoding;
    }
}
