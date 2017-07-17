package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;

public class CashAccountExtra
    implements CashAccount.Extra
{
    private final boolean income;
    private final int count;
    private final int minorCount;
    private final Currency currency;

    public CashAccountExtra(boolean ic, int c, int mc, Currency crc)
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