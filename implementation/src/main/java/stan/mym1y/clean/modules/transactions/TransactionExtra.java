package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.Transaction;

public class TransactionExtra
    implements Transaction.Extra
{
    static public Transaction.Extra create(String cat, Currency crc)
    {
        return new TransactionExtra(cat, crc);
    }

    private final String cashAccountTitle;
    private final Currency currency;

    private TransactionExtra(String cat, Currency crc)
    {
        cashAccountTitle = cat;
        currency = crc;
    }

    public String cashAccountTitle()
    {
        return cashAccountTitle;
    }
    public Currency currency()
    {
        return currency;
    }
}