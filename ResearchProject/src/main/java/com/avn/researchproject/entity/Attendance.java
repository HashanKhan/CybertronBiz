/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.avn.researchproject.entity;

/**
 *
 * @author chandima
 */
public class Attendance {

    private int id;
    private String itnumber;
    private int fingerprint;
    private String subject;
    private String hall;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItnumber() {
        return itnumber;
    }

    public void setItnumber(String itnumber) {
        this.itnumber = itnumber;
    }

    public int getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(int fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

}
