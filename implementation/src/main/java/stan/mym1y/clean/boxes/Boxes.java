package stan.mym1y.clean.boxes;

import stan.mym1y.clean.boxes.access.CashAccounts;
import stan.mym1y.clean.boxes.access.Transactions;
import stan.mym1y.clean.data.local.DAO;
import stan.mym1y.clean.data.local.access.CashAccountsAccess;
import stan.mym1y.clean.data.local.access.TransactionsAccess;

public class Boxes
    implements DAO
{
    private final TransactionsAccess transactionsAccess;
    private final CashAccountsAccess cashAccountsAccess;

    public Boxes(String path)
    {
        transactionsAccess = new Transactions(path);
        cashAccountsAccess = new CashAccounts(path);
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