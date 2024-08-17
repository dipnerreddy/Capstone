package com.capstone.bloodlink.entity;

import jakarta.persistence.*;

@Entity
public class BloodGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bloodBank_id", nullable = false)
    private BloodBank bloodBank;

    private int oPositive;
    private int oNegative;
    private int aPositive;
    private int aNegative;
    private int bPositive;
    private int bNegative;

    // Getters and Setters


    public BloodGroup() {
    }

    public BloodGroup(Long id, BloodBank bloodBank, int oPositive, int oNegative, int aPositive, int aNegative, int bPositive, int bNegative) {
        this.id = id;
        this.bloodBank = bloodBank;
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

    public BloodBank getBloodBank() {
        return bloodBank;
    }

    public void setBloodBank(BloodBank bloodBank) {
        this.bloodBank = bloodBank;
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
