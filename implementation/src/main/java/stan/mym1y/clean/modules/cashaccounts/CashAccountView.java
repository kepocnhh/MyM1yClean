package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;

public class CashAccountView
    implements CashAccountViewModel
{
    private final String title;

    public CashAccountView(String t)
    {
        title = t;
    }

    public String title()
    {
        return title;
    }
}