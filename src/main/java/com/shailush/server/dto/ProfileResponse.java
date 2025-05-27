package com.shailush.server.dto;

import java.sql.Date;

public class ProfileResponse {
    private Long id;

    private String name;

    private  String surname;

    private Date registrationDate;

    public ProfileResponse(Long id, String name, String surname, Date registrationDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.registrationDate = registrationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
