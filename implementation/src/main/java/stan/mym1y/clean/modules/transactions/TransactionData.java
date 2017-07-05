package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.Transaction;

public class TransactionData
    implements Transaction
{
    private final long id;
    private final long cashAccountId;
    private final long date;
    private final int count;
    private final int minorCount;

    public TransactionData(long i, long cai, long d, int c, int mc)
    {
        id = i;
        cashAccountId = cai;
        count = c;
        date = d;
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
    public int count()
    {
        return count;
    }
    public int minorCount()
    {
        return minorCount;
    }
}