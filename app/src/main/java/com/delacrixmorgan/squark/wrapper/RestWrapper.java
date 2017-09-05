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
        currencyList.add(new Currency("AUD", rates.getAUD()));
        currencyList.add(new Currency("BGN", rates.getBGN()));
        currencyList.add(new Currency("BRL", rates.getBRL()));
        currencyList.add(new Currency("CAD", rates.getCAD()));
        currencyList.add(new Currency("CHF", rates.getCHF()));

        currencyList.add(new Currency("CNY", rates.getCNY()));
        currencyList.add(new Currency("CZK", rates.getCZK()));
        currencyList.add(new Currency("DKK", rates.getDKK()));
        currencyList.add(new Currency("GBP", rates.getGBP()));
        currencyList.add(new Currency("HKD", rates.getHKD()));

        currencyList.add(new Currency("HRK", rates.getHRK()));
        currencyList.add(new Currency("HUF", rates.getHUF()));
        currencyList.add(new Currency("IDR", rates.getIDR()));
        currencyList.add(new Currency("ILS", rates.getILS()));
        currencyList.add(new Currency("INR", rates.getINR()));

        currencyList.add(new Currency("JPY", rates.getJPY()));
        currencyList.add(new Currency("KRW", rates.getKRW()));
        currencyList.add(new Currency("MXN", rates.getMXN()));
        currencyList.add(new Currency("MYR", rates.getMYR()));
        currencyList.add(new Currency("NOK", rates.getNOK()));

        currencyList.add(new Currency("NZD", rates.getNZD()));
        currencyList.add(new Currency("PHP", rates.getPHP()));
        currencyList.add(new Currency("PLN", rates.getPLN()));
        currencyList.add(new Currency("RON", rates.getRON()));
        currencyList.add(new Currency("RUB", rates.getRUB()));

        currencyList.add(new Currency("SEK", rates.getSEK()));
        currencyList.add(new Currency("SGD", rates.getSGD()));
        currencyList.add(new Currency("THB", rates.getTHB()));
        currencyList.add(new Currency("TRY", rates.getTRY()));
        currencyList.add(new Currency("USD", rates.getUSD()));

        currencyList.add(new Currency("ZAR", rates.getZAR()));

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
