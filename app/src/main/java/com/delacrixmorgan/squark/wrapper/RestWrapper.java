package com.delacrixmorgan.squark.wrapper;

import com.delacrixmorgan.squark.model.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Delacrix Morgan on 05/09/2017.
 */

public class RestWrapper {
    private String base, date;
    private CurrencyRate rates;

    public List<Currency> getCurrencyList() {
        List<Currency> currencyList = new ArrayList<>();
        currencyList.add(new Currency("AUD", "Australia", "Australian Dollar", rates.getAUD()));
        currencyList.add(new Currency("BGN", "Bulgaria", "Bulgarian Lev", rates.getBGN()));
        currencyList.add(new Currency("BRL", "Brazil", "Brazilian Real", rates.getBRL()));
        currencyList.add(new Currency("CAD", "Canada", "Canadian Dollar", rates.getCAD()));
        currencyList.add(new Currency("CHF", "Switzerland", "Swiss Franc", rates.getCHF()));

        currencyList.add(new Currency("CNY", "China", "Chinese Yuan", rates.getCNY()));
        currencyList.add(new Currency("CZK", "Czech_Republic", "Czech Koruna", rates.getCZK()));
        currencyList.add(new Currency("DKK", "Denmark", "Danish Krone", rates.getDKK()));
        currencyList.add(new Currency("GBP", "United_Kingdom", "British Pound", rates.getGBP()));
        currencyList.add(new Currency("HKD", "Hong_Kong", "Hong Kong Dollar", rates.getHKD()));

        currencyList.add(new Currency("HRK", "Croatia", "Croatian Kuna", rates.getHRK()));
        currencyList.add(new Currency("HUF", "Hungary", "Hungarian Forint", rates.getHUF()));
        currencyList.add(new Currency("IDR", "Indonesia", "Indonesian Rupiah", rates.getIDR()));
        currencyList.add(new Currency("ILS", "Israel", "Israeli New Shekel", rates.getILS()));
        currencyList.add(new Currency("INR", "India", "Indian Rupee", rates.getINR()));

        currencyList.add(new Currency("JPY", "Japan", "Japanese Yen", rates.getJPY()));
        currencyList.add(new Currency("KRW", "South_Korea", "South Korean Won", rates.getKRW()));
        currencyList.add(new Currency("MXN", "Mexico", "Mexican Peso", rates.getMXN()));
        currencyList.add(new Currency("MYR", "Malaysia", "Malaysian Ringgit", rates.getMYR()));
        currencyList.add(new Currency("NOK", "Norway", "Norwegian Krone", rates.getNOK()));

        currencyList.add(new Currency("NZD", "New_Zealand", "New Zealand Dollar", rates.getNZD()));
        currencyList.add(new Currency("PHP", "Philippines", "Philippine Peso", rates.getPHP()));
        currencyList.add(new Currency("PLN", "Poland", "Polish Zloty", rates.getPLN()));
        currencyList.add(new Currency("RON", "Romania", "Romanian Leu", rates.getRON()));
        currencyList.add(new Currency("RUB", "Russia", "Russian Ruble", rates.getRUB()));

        currencyList.add(new Currency("SEK", "Sweden", "Swedish Krona", rates.getSEK()));
        currencyList.add(new Currency("SGD", "Singapore", "Singaporean Dollar", rates.getSGD()));
        currencyList.add(new Currency("THB", "Thailand", "Thai Baht", rates.getTHB()));
        currencyList.add(new Currency("TRY", "Turkey", "Turkish Lira", rates.getTRY()));
        currencyList.add(new Currency("USD", "United_States", "United States Dollar", rates.getUSD()));

        currencyList.add(new Currency("ZAR", "South_Africa", "South African Rand", rates.getZAR()));

        return currencyList;
    }

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public CurrencyRate getRates() {
        return rates;
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
