package com.example.elad.gamaepsilonapp;

/**
 * Created by elad on 18/06/2017.
 */

public class Supervisor {
    String name;
    String workPlace;

    public Supervisor() {

    }

    public Supervisor(String name, String workPlace) {
        this.name = name;
        this.workPlace = workPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
