package com.example.elad.gamaepsilonapp;

/**
 * Created by elad on 18/06/2017.
 */

public class Supervisor {
    public String supervisorName;
    public String workPlace;

    public Supervisor() {

    }

    public Supervisor(String name, String workPlace) {
        this.supervisorName = name;
        this.workPlace = workPlace;
    }

    public String getName() {return supervisorName; }

    public void setName(String name) {
        this.supervisorName = name;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
