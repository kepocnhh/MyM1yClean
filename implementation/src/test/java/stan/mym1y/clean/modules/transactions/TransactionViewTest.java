package stan.mym1y.clean.modules.transactions;

import org.junit.Test;

import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.utils.MainTest;

import static org.junit.Assert.assertEquals;

public class TransactionViewTest
        extends MainTest
{
    @Test
    public void checkModel()
    {
        int count = nextInt();
        long date = Integer.MAX_VALUE;
        date += nextInt();
        TransactionViewModel transaction = new TransactionView(
                count
                ,date
        );
        assertEquals(transaction.getCount(), count);
        assertEquals(transaction.getDate(), date);
    }
}