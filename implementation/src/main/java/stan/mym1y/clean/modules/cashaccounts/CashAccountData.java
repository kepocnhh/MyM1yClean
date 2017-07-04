package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public class CashAccountData
    implements CashAccount
{
    private final long id;
    private final String uuid;
    private final String currencyCodeNumber;
    private final String title;

    public CashAccountData(long i, String ui, String ccn, String t)
    {
        id = i;
        uuid = ui;
        currencyCodeNumber = ccn;
        title = t;
    }

    public long id()
    {
        return id;
    }
    public String uuid()
    {
        return uuid;
    }
    public String currencyCodeNumber()
    {
        return currencyCodeNumber;
    }
    public String title()
    {
        return title;
    }
}