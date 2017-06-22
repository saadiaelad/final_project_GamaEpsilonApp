package com.example.elad.gamaepsilonapp;

/**
 * Created by elad on 18/06/2017.
 */
public class User {
    String firsName;
    String lastName;
    String phoneNumber;
    String permission;
    String email;
    boolean constractor;

    public User(){

    }

    public User(String firsName, String lastName, String phoneNumber, String permission,
                String email, boolean constractor, int role) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.permission = permission;
        this.email = email;
        this.constractor = constractor;
    }

    public String getFirsName() {
        return firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPermission() {
        return permission;
    }

    public String getEmail() {
        return email;
    }

    public boolean isConstractor() {
        return constractor;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setConstractor(boolean constractor) {
        this.constractor = constractor;
    }
}




