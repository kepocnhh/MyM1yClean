package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;

public class CashAccountView
    implements CashAccountViewModel
{
    private final String title;
    private final String currencyCodeNumber;

    public CashAccountView(String t, String ccn)
    {
        title = t;
        currencyCodeNumber = ccn;
    }

    public String title()
    {
        return title;
    }
    public String currencyCodeNumber()
    {
        return currencyCodeNumber;
    }
}