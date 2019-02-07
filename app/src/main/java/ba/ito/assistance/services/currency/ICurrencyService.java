package ba.ito.assistance.services.currency;

import ba.ito.assistance.model.gas_stations.CurrencyEnum;

public interface ICurrencyService {

    String format(CurrencyEnum currencyEnum,double value);
}
