package com.arcmedtek.e_puskesmas.model;

public class HistoryModel {
    String day, date, nik, poly, complaint, diagnosis, medicine, note, pasFirstName, pasMiddleName, pasLastName, dokFrontDegree, dokFirstName, dokMiddleName, dokLastName, dokLastDegree;
    boolean expand;

    public HistoryModel() {
    }

    public HistoryModel(String day, String date, String nik, String poly, String complaint, String diagnosis, String medicine, String note, String pasFirstName, String pasMiddleName, String pasLastName, String dokFrontDegree, String dokFirstName, String dokMiddleName, String dokLastName, String dokLastDegree) {
        this.day = day;
        this.date = date;
        this.nik = nik;
        this.poly = poly;
        this.complaint = complaint;
        this.diagnosis = diagnosis;
        this.medicine = medicine;
        this.note = note;
        this.pasFirstName = pasFirstName;
        this.pasMiddleName = pasMiddleName;
        this.pasLastName = pasLastName;
        this.dokFrontDegree = dokFrontDegree;
        this.dokFirstName = dokFirstName;
        this.dokMiddleName = dokMiddleName;
        this.dokLastName = dokLastName;
        this.dokLastDegree = dokLastDegree;
        this.expand = false;
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

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getPoly() {
        return poly;
    }

    public void setPoly(String poly) {
        this.poly = poly;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
