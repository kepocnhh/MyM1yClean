package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.utils.MainTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionTest
        extends MainTest
{
    @Test
    public void checkModel()
    {
        int count = nextInt();
        long date = Long.MAX_VALUE;
        date += nextInt();
        int id = nextInt();
        TransactionModel transaction = new Transaction(
                id
                ,count
                ,date
        );
        assertEquals(transaction.getId(), id);
        assertEquals(transaction.getCount(), count);
        assertEquals(transaction.getDate(), date);
    }
}