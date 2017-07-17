package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.Transaction;

public class TransactionData
    implements Transaction
{
    private final long id;
    private final long cashAccountId;
    private final long date;
    private final boolean income;
    private final int count;
    private final int minorCount;

    public TransactionData(long i, long cai, long d, boolean ic, int c, int mc)
    {
        id = i;
        cashAccountId = cai;
        date = d;
        income = ic;
        count = c;
        minorCount = mc;
    }

    public long id()
    {
        return id;
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