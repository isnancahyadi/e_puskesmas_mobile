package com.arcmedtek.e_puskesmas.model;

public class DoctorScheduleModel {
    String sip, day, poly, frontDegree, firstName, middleName, lastName, lastDegree;

    public DoctorScheduleModel() {
    }

    public DoctorScheduleModel(String sip, String day, String poly, String frontDegree, String firstName, String middleName, String lastName, String lastDegree) {
        this.sip = sip;
        this.day = day;
        this.poly = poly;
        this.frontDegree = frontDegree;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lastDegree = lastDegree;
    }

    public String getSip() {
        return sip;
    }

    public void setSip(String sip) {
        this.sip = sip;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPoly() {
        return poly;
    }

    public void setPoly(String poly) {
        this.poly = poly;
    }

    public String getFrontDegree() {
        return frontDegree;
    }

    public void setFrontDegree(String frontDegree) {
        this.frontDegree = frontDegree;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree;
    }
}
