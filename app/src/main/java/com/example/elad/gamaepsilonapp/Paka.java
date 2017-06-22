package com.example.elad.gamaepsilonapp;

import java.util.Date;

/**
 * Created by elad on 15/06/2017.
 */

public class Paka {
    int id;
    int pakaNum;
    String address;
    int openOrClose;
    Date openDate;
    Date startingDate;
    Date closingDate;
    int teamLeader;
    int supervisor;
    int periorty;
    String className;
    int tat;
    int[] workers;
    int[] workPerfomed;
    Float price;
    String commit;

    public Paka(){

    }

    public Paka(int id, int pakaNum, String address,
                int openOrClose, Date openDate, Date startingDate,
                Date closingDate, int teamLeader, int supervisor,
                int periorty, String className, int tat, int[] workers,
                int[] workPerfomed, Float price, String commit) {

        this.id = id;
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

    public int getId() {
        return id;
    }

    public int getPakaNum() {
        return pakaNum;
    }

    public String getAddress() {
        return address;
    }

    public int getOpenOrClose() {
        return openOrClose;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public int getTeamLeader() {
        return teamLeader;
    }

    public int getSupervisor() {
        return supervisor;
    }

    public int getPeriorty() {
        return periorty;
    }

    public String getClassName() {
        return className;
    }

    public int getTat() {
        return tat;
    }

    public int[] getWorkers() {
        return workers;
    }

    public int[] getWorkPerfomed() {
        return workPerfomed;
    }

    public Float getPrice() {
        return price;
    }

    public String getCommit() {
        return commit;
    }

    public void setId(int id) { this.id = id; }

    public void setPakaNum(int pakaNum) {
        this.pakaNum = pakaNum;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpenOrClose(int openOrClose) {
        this.openOrClose = openOrClose;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public void setTeamLeader(int teamLeader) {
        this.teamLeader = teamLeader;
    }

    public void setSupervisor(int supervisor) {
        this.supervisor = supervisor;
    }

    public void setPeriorty(int periorty) {
        this.periorty = periorty;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTat(int tat) {
        this.tat = tat;
    }

    public void setWorkers(int[] workers) {
        this.workers = workers;
    }

    public void setWorkPerfomed(int[] workPerfomed) {
        this.workPerfomed = workPerfomed;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}
