package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.TransactionViewModel;

public class TransactionView
    implements TransactionViewModel
{
    private int count;
    private long date;

    public TransactionView(int c, long d)
    {
        count = c;
        date = d;
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