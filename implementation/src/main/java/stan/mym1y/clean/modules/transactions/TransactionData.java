package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.Transaction;

public class TransactionData
    implements Transaction
{
    private int id;
    private int count;
    private long date;

    public TransactionData(int i, int c, long d)
    {
        id = i;
        count = c;
        date = d;
    }

    public int getId()
    {
        return id;
    }
    public long getDate()
    {
        return date;
    }
    public int getCount()
    {
        return count;
    }
}