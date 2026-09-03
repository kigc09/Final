package com.example.kgarciaassignment7;

public enum UserRole {
    REGULAR, MANAGER, ADMIN, SUPER_ADMIN;

    @Override
    public String toString(){
        return switch(this.ordinal()){
            case 0 -> "Regular";
            case 1 -> "Manager";
            case 2 -> "Admin";
            default -> "Super Admin";
        };
    }
}
