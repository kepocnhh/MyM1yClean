package stan.mym1y.clean.boxes;

import stan.mym1y.clean.boxes.access.CashAccounts;
import stan.mym1y.clean.boxes.access.Currencies;
import stan.mym1y.clean.boxes.access.Transactions;
import stan.mym1y.clean.data.local.DAO;
import stan.mym1y.clean.data.local.access.CashAccountsAccess;
import stan.mym1y.clean.data.local.access.CurrenciesAccess;
import stan.mym1y.clean.data.local.access.TransactionsAccess;

public class Boxes
    implements DAO
{
    private final CurrenciesAccess currenciesAccess;
    private final TransactionsAccess transactionsAccess;
    private final CashAccountsAccess cashAccountsAccess;

    public Boxes(String path)
    {
        currenciesAccess = new Currencies(path);
        transactionsAccess = new Transactions(path);
        cashAccountsAccess = new CashAccounts(path);
    }

    public CurrenciesAccess currenciesAccess()
    {
        return currenciesAccess;
    }
    public TransactionsAccess transactionsAccess()
    {
        return transactionsAccess;
    }
    public CashAccountsAccess cashAccountsAccess()
    {
        return cashAccountsAccess;
    }
}