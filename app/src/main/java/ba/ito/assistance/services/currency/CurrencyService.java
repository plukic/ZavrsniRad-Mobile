package ba.ito.assistance.services.currency;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import ba.ito.assistance.model.gas_stations.CurrencyEnum;

public class CurrencyService implements ICurrencyService {
    @Inject
    public CurrencyService() {
    }

    @Override
    public String format(CurrencyEnum currencyEnum, double value) {

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(""); // Don't use null.

        formatter.setDecimalFormatSymbols(symbols);
//        System.out.println(formatter.format(12.3456)); // 12.35
        return formatter.format(value) + " " +getCurrencySimbol(currencyEnum);
    }

    private String getCurrencySimbol(CurrencyEnum currencyEnum) {
        switch (currencyEnum) {
            case KM:
                return "KM";
            case KN:
                return "KN";
            case EURO:
                return "€";
        }
        return "";
    }
}
