package com.delacrixmorgan.squark.wrapper;

import com.delacrixmorgan.squark.model.Currency;

import io.realm.Realm;

/**
 * Created by Delacrix Morgan on 05/09/2017.
 */

public class APIWrapper {
    private String base, date;
    private CurrencyRate rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public CurrencyRate getRates() {
        return rates;
    }

    public void updateCurrencyList() {
        Realm realm = Realm.getDefaultInstance();

        addTask(realm, new Currency("AUD", "Australia", "Australian Dollar", rates.getAUD()));
        addTask(realm, new Currency("BGN", "Bulgaria", "Bulgarian Lev", rates.getBGN()));
        addTask(realm, new Currency("BRL", "Brazil", "Brazilian Real", rates.getBRL()));
        addTask(realm, new Currency("CAD", "Canada", "Canadian Dollar", rates.getCAD()));
        addTask(realm, new Currency("CHF", "Switzerland", "Swiss Franc", rates.getCHF()));

        addTask(realm, new Currency("CNY", "China", "Chinese Yuan", rates.getCNY()));
        addTask(realm, new Currency("CZK", "Czech_Republic", "Czech Koruna", rates.getCZK()));
        addTask(realm, new Currency("DKK", "Denmark", "Danish Krone", rates.getDKK()));
        addTask(realm, new Currency("GBP", "United_Kingdom", "British Pound", rates.getGBP()));
        addTask(realm, new Currency("HKD", "Hong_Kong", "Hong Kong Dollar", rates.getHKD()));

        addTask(realm, new Currency("HRK", "Croatia", "Croatian Kuna", rates.getHRK()));
        addTask(realm, new Currency("HUF", "Hungary", "Hungarian Forint", rates.getHUF()));
        addTask(realm, new Currency("IDR", "Indonesia", "Indonesian Rupiah", rates.getIDR()));
        addTask(realm, new Currency("ILS", "Israel", "Israeli New Shekel", rates.getILS()));
        addTask(realm, new Currency("INR", "India", "Indian Rupee", rates.getINR()));

        addTask(realm, new Currency("JPY", "Japan", "Japanese Yen", rates.getJPY()));
        addTask(realm, new Currency("KRW", "South_Korea", "South Korean Won", rates.getKRW()));
        addTask(realm, new Currency("MXN", "Mexico", "Mexican Peso", rates.getMXN()));
        addTask(realm, new Currency("MYR", "Malaysia", "Malaysian Ringgit", rates.getMYR()));
        addTask(realm, new Currency("NOK", "Norway", "Norwegian Krone", rates.getNOK()));

        addTask(realm, new Currency("NZD", "New_Zealand", "New Zealand Dollar", rates.getNZD()));
        addTask(realm, new Currency("PHP", "Philippines", "Philippine Peso", rates.getPHP()));
        addTask(realm, new Currency("PLN", "Poland", "Polish Zloty", rates.getPLN()));
        addTask(realm, new Currency("RON", "Romania", "Romanian Leu", rates.getRON()));
        addTask(realm, new Currency("RUB", "Russia", "Russian Ruble", rates.getRUB()));

        addTask(realm, new Currency("SEK", "Sweden", "Swedish Krona", rates.getSEK()));
        addTask(realm, new Currency("SGD", "Singapore", "Singaporean Dollar", rates.getSGD()));
        addTask(realm, new Currency("THB", "Thailand", "Thai Baht", rates.getTHB()));
        addTask(realm, new Currency("TRY", "Turkey", "Turkish Lira", rates.getTRY()));
        addTask(realm, new Currency("USD", "United_States", "United States Dollar", rates.getUSD()));

        addTask(realm, new Currency("ZAR", "South_Africa", "South African Rand", rates.getZAR()));
    }

    private void addTask(Realm realm, Currency currency) {
        realm.beginTransaction();

        Currency queryCurrency = realm.where(Currency.class).equalTo("code", currency.getCode()).findFirst();

        if (queryCurrency == null) {
            // New Currency
            realm.copyToRealm(currency);

        } else {
            // Update Currency
            queryCurrency.setRate(currency.getRate());
        }

        realm.commitTransaction();
    }

    public static class CurrencyRate {
        private double AUD;
        private double BGN;
        private double BRL;
        private double CAD;
        private double CHF;
        private double CNY;
        private double CZK;
        private double DKK;
        private double GBP;
        private double HKD;
        private double HRK;
        private double HUF;
        private double IDR;
        private double ILS;
        private double INR;
        private double JPY;
        private double KRW;
        private double MXN;
        private double MYR;
        private double NOK;
        private double NZD;
        private double PHP;
        private double PLN;
        private double RON;
        private double RUB;
        private double SEK;
        private double SGD;
        private double THB;
        private double TRY;
        private double USD;
        private double ZAR;

        public double getAUD() {
            return AUD;
        }

        public double getBGN() {
            return BGN;
        }

        public double getBRL() {
            return BRL;
        }

        public double getCAD() {
            return CAD;
        }

        public double getCHF() {
            return CHF;
        }

        public double getCNY() {
            return CNY;
        }

        public double getCZK() {
            return CZK;
        }

        public double getDKK() {
            return DKK;
        }

        public double getGBP() {
            return GBP;
        }

        public double getHKD() {
            return HKD;
        }

        public double getHRK() {
            return HRK;
        }

        public double getHUF() {
            return HUF;
        }

        public double getIDR() {
            return IDR;
        }

        public double getILS() {
            return ILS;
        }

        public double getINR() {
            return INR;
        }

        public double getJPY() {
            return JPY;
        }

        public double getKRW() {
            return KRW;
        }

        public double getMXN() {
            return MXN;
        }

        public double getMYR() {
            return MYR;
        }

        public double getNOK() {
            return NOK;
        }

        public double getNZD() {
            return NZD;
        }

        public double getPHP() {
            return PHP;
        }

        public double getPLN() {
            return PLN;
        }

        public double getRON() {
            return RON;
        }

        public double getRUB() {
            return RUB;
        }

        public double getSEK() {
            return SEK;
        }

        public double getSGD() {
            return SGD;
        }

        public double getTHB() {
            return THB;
        }

        public double getTRY() {
            return TRY;
        }

        public double getUSD() {
            return USD;
        }

        public double getZAR() {
            return ZAR;
        }
    }
}
