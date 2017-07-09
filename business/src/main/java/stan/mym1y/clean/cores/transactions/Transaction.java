package stan.mym1y.clean.cores.transactions;

import stan.mym1y.clean.cores.currencies.Currency;

public interface Transaction
{
    long id();
    long cashAccountId();
    long date();
    int count();
    int minorCount();

    interface Extra
    {
        String cashAccountTitle();
        Currency currency();
    }
}