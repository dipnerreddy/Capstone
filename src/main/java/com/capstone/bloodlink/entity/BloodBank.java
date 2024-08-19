package com.capstone.bloodlink.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "bloodBank")
public class BloodBank {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String bloodBankName;
    private String password;
    private String email;
    private String location;
    private int oPositive;
    private int oNegative;
    private int aPositive;
    private int aNegative;
    private int bPositive;
    private int bNegative;


    public BloodBank() {
    }

    public BloodBank(String bloodBankName, String password, String email, String location) {
        this.bloodBankName = bloodBankName;
        this.password = password;
        this.email = email;
        this.location = location;
    }

    public BloodBank( String email, int oPositive, int oNegative, int aPositive, int aNegative, int bPositive, int bNegative) {
        this.email = email;
        this.oPositive = oPositive;
        this.oNegative = oNegative;
        this.aPositive = aPositive;
        this.aNegative = aNegative;
        this.bPositive = bPositive;
        this.bNegative = bNegative;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBloodBankName() {
        return bloodBankName;
    }

    public void setBloodBankName(String bloodBankName) {
        this.bloodBankName = bloodBankName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getoPositive() {
        return oPositive;
    }

    public void setoPositive(int oPositive) {
        this.oPositive = oPositive;
    }

    public int getoNegative() {
        return oNegative;
    }

    public void setoNegative(int oNegative) {
        this.oNegative = oNegative;
    }

    public int getaPositive() {
        return aPositive;
    }

    public void setaPositive(int aPositive) {
        this.aPositive = aPositive;
    }

    public int getaNegative() {
        return aNegative;
    }

    public void setaNegative(int aNegative) {
        this.aNegative = aNegative;
    }

    public int getbPositive() {
        return bPositive;
    }

    public void setbPositive(int bPositive) {
        this.bPositive = bPositive;
    }

    public int getbNegative() {
        return bNegative;
    }

    public void setbNegative(int bNegative) {
        this.bNegative = bNegative;
    }
}
