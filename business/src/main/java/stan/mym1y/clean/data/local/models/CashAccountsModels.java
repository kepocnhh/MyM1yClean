package stan.mym1y.clean.data.local.models;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public interface CashAccountsModels
{
    interface CashAccounts
    {
        List<CashAccount> getAll();
        List<CashAccount> getAllFromCurrencyCodeNumber(String codeNumber);
        CashAccount get(long id);
        void remove(long id);
        void add(CashAccount cashAccount);
        void clear();
    }
}