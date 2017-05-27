package stan.mym1y.clean.data.local.models;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public interface CashAccountsModels
{
    interface CashAccounts
    {
        List<CashAccount> getAll();
        CashAccount get(long id);
        void add(CashAccount cashAccount);
    }
}