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

public enum ItemTitle {
    FOOD_DRINK, APPAREL, ACCESSORY, BOOK, SCHOOL_MATERIAL;

    @Override
    public String toString(){
        return switch(this.ordinal()){
            case 0 -> "Food & Drink";
            case 1 -> "Apparel";
            case 2 -> "Accessory";
            case 3 -> "Book";
            default -> "School Material";
        };
    }
}
