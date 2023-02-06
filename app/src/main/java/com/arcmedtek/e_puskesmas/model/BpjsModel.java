package com.arcmedtek.e_puskesmas.model;

public class BpjsModel {
    String idBpjs, nik, faksesLevel, faskesLevelName, photoBpjs;

    public BpjsModel() {
    }

    public BpjsModel(String idBpjs, String nik, String faksesLevel, String faskesLevelName, String photoBpjs) {
        this.idBpjs = idBpjs;
        this.nik = nik;
        this.faksesLevel = faksesLevel;
        this.faskesLevelName = faskesLevelName;
        this.photoBpjs = photoBpjs;
    }

    public String getIdBpjs() {
        return idBpjs;
    }

    public void setIdBpjs(String idBpjs) {
        this.idBpjs = idBpjs;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getFaksesLevel() {
        return faksesLevel;
    }

    public void setFaksesLevel(String faksesLevel) {
        this.faksesLevel = faksesLevel;
    }

    public String getFaskesLevelName() {
        return faskesLevelName;
    }

    public void setFaskesLevelName(String faskesLevelName) {
        this.faskesLevelName = faskesLevelName;
    }

    public String getPhotoBpjs() {
        return photoBpjs;
    }

    public void setPhotoBpjs(String photoBpjs) {
        this.photoBpjs = photoBpjs;
    }
}
