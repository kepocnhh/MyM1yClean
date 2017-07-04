package stan.mym1y.clean.data.local;

import stan.mym1y.clean.data.local.access.CashAccountsAccess;
import stan.mym1y.clean.data.local.access.CurrenciesAccess;
import stan.mym1y.clean.data.local.access.TransactionsAccess;

public interface DAO
{
    CurrenciesAccess currenciesAccess();
    TransactionsAccess transactionsAccess();
    CashAccountsAccess cashAccountsAccess();
}