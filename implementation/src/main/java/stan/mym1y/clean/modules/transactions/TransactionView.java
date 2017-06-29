package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.TransactionViewModel;

public class TransactionView
    implements TransactionViewModel
{
    private final long cashAccountId;
    private final long date;
    private final int count;

    public TransactionView(long cai, long d, int c)
    {
        cashAccountId = cai;
        date = d;
        count = c;
    }

    public long cashAccountId()
    {
        return cashAccountId;
    }
    public long date()
    {
        return date;
    }
    public int count()
    {
        return count;
    }
}