package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.TransactionViewModel;

public class TransactionView
    implements TransactionViewModel
{
    private final long cashAccountId;
    private final long date;
    private final boolean income;
    private final int count;
    private final int minorCount;

    public TransactionView(long cai, long d, boolean ic, int c, int mc)
    {
        cashAccountId = cai;
        date = d;
        income = ic;
        count = c;
        minorCount = mc;
    }

    public long cashAccountId()
    {
        return cashAccountId;
    }
    public long date()
    {
        return date;
    }
    public boolean income()
    {
        return income;
    }
    public int count()
    {
        return count;
    }
    public int minorCount()
    {
        return minorCount;
    }
}