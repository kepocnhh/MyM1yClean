package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;

public class CashAccountExtra
    implements CashAccount.Extra
{
    static public CashAccount.Extra create(boolean ic, int c, int mc, Currency crc)
    {
        return new CashAccountExtra(ic, c, mc, crc);
    }

    private final boolean income;
    private final int count;
    private final int minorCount;
    private final Currency currency;

    private CashAccountExtra(boolean ic, int c, int mc, Currency crc)
    {
        income = ic;
        count = c;
        minorCount = mc;
        currency = crc;
    }

    public boolean income()
    {
        return income;
    }
    public int count()
    {
        return count;
    }
    public int minorCount()
    {
        return minorCount;
    }
    public Currency currency()
    {
        return currency;
    }
}