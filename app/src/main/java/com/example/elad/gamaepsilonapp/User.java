package com.example.elad.gamaepsilonapp;

/**
 * Created by elad on 18/06/2017.
 */
public class User {
    String firstName;
    String lastName;
    String phoneNumber;
    String permission;
    String userMail;
    boolean constractor;

    public User(){

    }

    public User(String firstName, String lastName, String phoneNumber, String permission,
                String email, boolean constractor, int role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.permission = permission;
        this.userMail = email;
        this.constractor = constractor;
    }

    public String getFirstName() {
        return firstName;
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
        return userMail;
    }

    public boolean isConstractor() {
        return constractor;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
        this.userMail = email;
    }

    public void setConstractor(boolean constractor) {
        this.constractor = constractor;
    }
}




