package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public class CashAccountExtra
    implements CashAccount.Extra
{
    private final int balance;

    public CashAccountExtra(int balance)
    {
        this.balance = balance;
    }

    public int balance()
    {
        return balance;
    }
}