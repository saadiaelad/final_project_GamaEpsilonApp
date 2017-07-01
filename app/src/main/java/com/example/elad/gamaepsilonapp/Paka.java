package com.example.elad.gamaepsilonapp;

import java.util.ArrayList;

/**
 * Created by elad on 15/06/2017.
 */

public class Paka {
    String pakaNum;
    String address;
    boolean openOrClose;
    String  openDate;
    String  startingDate;
    String  closingDate;
    ArrayList<String> teamLeader;
    String supervisor;
    String periorty;
    String className;
    boolean tat;
    ArrayList<String> workers;
    ArrayList<WorkPerfomed> workPerfomed;
    String price;
    String commit;

    public Paka() {

    }

    public Paka(String pakaNum, String address, boolean openOrClose, String  openDate, String startingDate,
                String closingDate, ArrayList<String> teamLeader, String supervisor, String periorty,
                String className, boolean tat, ArrayList<String> workers, ArrayList<WorkPerfomed> workPerfomed,
                String price, String commit) {

        this.pakaNum = pakaNum;
        this.address = address;
        this.openOrClose = openOrClose;
        this.openDate = openDate;
        this.startingDate = startingDate;
        this.closingDate = closingDate;
        this.teamLeader = teamLeader;
        this.supervisor = supervisor;
        this.periorty = periorty;
        this.className = className;
        this.tat = tat;
        this.workers = workers;
        this.workPerfomed = workPerfomed;
        this.price = price;
        this.commit = commit;
    }

    public String getPakaNum() {
        return pakaNum;
    }

    public void setPakaNum(String pakaNum) {
        this.pakaNum = pakaNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOpenOrClose() {
        return openOrClose;
    }

    public void setOpenOrClose(boolean openOrClose) {
        this.openOrClose = openOrClose;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public ArrayList<String> getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(ArrayList<String> teamLeader) {
        this.teamLeader = teamLeader;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getPeriorty() {
        return periorty;
    }

    public void setPeriorty(String periorty) {
        this.periorty = periorty;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isTat() {
        return tat;
    }

    public void setTat(boolean tat) {
        this.tat = tat;
    }

    public ArrayList<String> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<String> workers) {
        this.workers = workers;
    }

    public ArrayList<WorkPerfomed> getWorkPerfomed() {
        return workPerfomed;
    }

    public void setWorkPerfomed(ArrayList<WorkPerfomed> workPerfomed) {
        this.workPerfomed = workPerfomed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}

