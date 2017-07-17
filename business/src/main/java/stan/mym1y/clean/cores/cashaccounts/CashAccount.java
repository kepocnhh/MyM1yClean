package stan.mym1y.clean.cores.cashaccounts;

import stan.mym1y.clean.cores.currencies.Currency;

public interface CashAccount
{
    long id();
    String uuid();
    String currencyCodeNumber();
    String title();

    interface Extra
    {
        boolean income();
        int count();
        int minorCount();
        Currency currency();
    }
}