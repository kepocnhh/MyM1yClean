package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.Transaction;

public class TransactionExtra
    implements Transaction.Extra
{
    private final String cashAccountTitle;

    public TransactionExtra(String cat)
    {
        cashAccountTitle = cat;
    }

    public String cashAccountTitle()
    {
        return cashAccountTitle;
    }
}