package com.delacrixmorgan.squark.deprecrated.model;

import io.realm.CurrencyRealmProxy;
import io.realm.RealmObject;

/**
 * Created by Delacrix Morgan on 05/09/2017.
 */

@org.parceler.Parcel(implementations = {CurrencyRealmProxy.class},
        value = org.parceler.Parcel.Serialization.BEAN,
        analyze = {Currency.class})

public class Currency extends RealmObject {
    private String code, country, description;
    private double rate;

    public Currency() {

    }

    public Currency(String code, String country, String description, double rate) {
        this.code = code;
        this.country = country;
        this.description = description;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}