package com.arcmedtek.e_puskesmas.model;

public class QueueModel {
    String queueNum, nik, pasFirstName, pasMiddleName, pasLastName, pasGender, poly, dokFrontDegree, dokFirstName, dokMiddleName, dokLastName, dokLastDegree, day, date, patientType, status;

    public QueueModel() {
    }

    public QueueModel(String queueNum, String nik, String pasFirstName, String pasMiddleName, String pasLastName, String pasGender, String poly, String dokFrontDegree, String dokFirstName, String dokMiddleName, String dokLastName, String dokLastDegree, String day, String date, String patientType, String status) {
        this.queueNum = queueNum;
        this.nik = nik;
        this.pasFirstName = pasFirstName;
        this.pasMiddleName = pasMiddleName;
        this.pasLastName = pasLastName;
        this.pasGender = pasGender;
        this.poly = poly;
        this.dokFrontDegree = dokFrontDegree;
        this.dokFirstName = dokFirstName;
        this.dokMiddleName = dokMiddleName;
        this.dokLastName = dokLastName;
        this.dokLastDegree = dokLastDegree;
        this.day = day;
        this.date = date;
        this.patientType = patientType;
        this.status = status;
    }

    public String getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(String queueNum) {
        this.queueNum = queueNum;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getPasFirstName() {
        return pasFirstName;
    }

    public void setPasFirstName(String pasFirstName) {
        this.pasFirstName = pasFirstName;
    }

    public String getPasMiddleName() {
        return pasMiddleName;
    }

    public void setPasMiddleName(String pasMiddleName) {
        this.pasMiddleName = pasMiddleName;
    }

    public String getPasLastName() {
        return pasLastName;
    }

    public void setPasLastName(String pasLastName) {
        this.pasLastName = pasLastName;
    }

    public String getPasGender() {
        return pasGender;
    }

    public void setPasGender(String pasGender) {
        this.pasGender = pasGender;
    }

    public String getPoly() {
        return poly;
    }

    public void setPoly(String poly) {
        this.poly = poly;
    }

    public String getDokFrontDegree() {
        return dokFrontDegree;
    }

    public void setDokFrontDegree(String dokFrontDegree) {
        this.dokFrontDegree = dokFrontDegree;
    }

    public String getDokFirstName() {
        return dokFirstName;
    }

    public void setDokFirstName(String dokFirstName) {
        this.dokFirstName = dokFirstName;
    }

    public String getDokMiddleName() {
        return dokMiddleName;
    }

    public void setDokMiddleName(String dokMiddleName) {
        this.dokMiddleName = dokMiddleName;
    }

    public String getDokLastName() {
        return dokLastName;
    }

    public void setDokLastName(String dokLastName) {
        this.dokLastName = dokLastName;
    }

    public String getDokLastDegree() {
        return dokLastDegree;
    }

    public void setDokLastDegree(String dokLastDegree) {
        this.dokLastDegree = dokLastDegree;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
