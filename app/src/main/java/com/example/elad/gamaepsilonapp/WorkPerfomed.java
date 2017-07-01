package com.example.elad.gamaepsilonapp;

/**
 * Created by elad on 30/06/2017.
 */

public class WorkPerfomed {

    String workName;
    String workNameArabic;
    String price;
    String amount;

    public WorkPerfomed() {
    }

    public WorkPerfomed(String workName, String workNameArabic, String price, String amount) {
        this.workName = workName;
        this.workNameArabic = workNameArabic;
        this.price = price;
        this.amount = amount;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkNameArabic() {
        return workNameArabic;
    }

    public void setWorkNameArabic(String workNameArabic) {
        this.workNameArabic = workNameArabic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
