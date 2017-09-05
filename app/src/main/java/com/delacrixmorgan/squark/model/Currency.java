package com.delacrixmorgan.squark.model;

/**
 * Created by Delacrix Morgan on 05/09/2017.
 */

public class Currency {
    private String name;
    private double rate;

    public Currency(String name, double rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }
}
