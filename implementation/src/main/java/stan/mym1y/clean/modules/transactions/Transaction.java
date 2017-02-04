package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.TransactionModel;

public class Transaction
    implements TransactionModel
{
    private int count;
    private long date;
    private int id;

    public Transaction(int id, int count, long date)
    {
        this.count = count;
        this.date = date;
        this.id = id;
    }

    @Override
    public int getId()
    {
        return id;
    }
    @Override
    public long getDate()
    {
        return date;
    }
    @Override
    public int getCount()
    {
        return count;
    }
}