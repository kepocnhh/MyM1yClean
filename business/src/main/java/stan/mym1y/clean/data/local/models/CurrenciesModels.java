package stan.mym1y.clean.data.local.models;

import java.util.List;

import stan.mym1y.clean.cores.currencies.Currency;

public interface CurrenciesModels
{
    interface Currencies
    {
        List<Currency> getAll();
        Currency get(String codeName);
        void remove(String codeName);
        void add(Currency currency);
        void add(List<Currency> currencies);
        void clear();
    }
}