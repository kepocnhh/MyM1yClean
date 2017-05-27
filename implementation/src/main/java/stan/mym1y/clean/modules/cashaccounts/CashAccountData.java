package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public class CashAccountData
    implements CashAccount
{
    private final long id;
    private final String title;

    public CashAccountData(long i, String t)
    {
        id = i;
        title = t;
    }

    public long id()
    {
        return id;
    }
    public String title()
    {
        return title;
    }
}