package com.example.kgarciaassignment7;

public class User {
    int id;
    String name;
    String contact;
    private String email;
    String userLogin;
    String password;
    private String createdBy;
    private String createdAt;

    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean equals(Object o2) {
        if (o2 instanceof User u2) {
            return id== u2.getId();
        }
        return false;
    }

    public String getUserLogin(){ return userLogin; }

    public void setUserLogin(String userLogin) { this.userLogin = userLogin; }

    public String getPassword(){ return password; }

    public void setPassword(String password) { this.password = password; }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}

