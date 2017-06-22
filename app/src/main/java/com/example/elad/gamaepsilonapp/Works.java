package com.example.elad.gamaepsilonapp;

/**
 * Created by elad on 18/06/2017.
 */

public class Works {
    String abbriviatedName;
    String arbicName;
    double cost;
    int id;
    String fullName;
    String units;
    String workNum;

    public Works() {

    }

    public Works(String abbriviatedName, String arbicName, double cost, int id, String fullName, String units, String workNum) {
        this.abbriviatedName = abbriviatedName;
        this.arbicName = arbicName;
        this.cost = cost;
        this.id = id;
        this.fullName = fullName;
        this.units = units;
        this.workNum = workNum;
    }

    public String getAbbriviatedName() {
        return abbriviatedName;
    }

    public void setAbbriviatedName(String abbriviatedName) {
        this.abbriviatedName = abbriviatedName;
    }

    public String getArbicName() {
        return arbicName;
    }

    public void setArbicName(String arbicName) {
        this.arbicName = arbicName;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }
}
