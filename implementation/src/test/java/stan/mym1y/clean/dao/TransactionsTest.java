package stan.mym1y.clean.dao;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.utils.RobolectricTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TransactionsTest
    extends RobolectricTest
{
    private Models.Transactions transactions;

    @Before
    public void before()
    {
        transactions = new Boxes(RuntimeEnvironment.application.getApplicationContext().getFilesDir().getAbsolutePath()).getTransactions();
    }

    @Test
    public void checkTransactions()
    {
        ListModel<TransactionModel> listModel = transactions.getAll();
        assertNotNull(listModel);
        assertEquals(listModel.size(), 0);
    }
    @Test
    public void checkInsertTransaction()
    {
        int count = nextInt();
        long date = Integer.MAX_VALUE;
        date += nextInt();
        int id = nextInt();
        TransactionModel transaction = new Transaction(
                id
                ,count
                ,date
        );
        transactions.add(transaction);
        ListModel<TransactionModel> listModel = transactions.getAll();
        assertEquals("listModel size != 1 after add", listModel.size(), 1);
        TransactionModel newTransaction = listModel.get(0);
        assertEquals(newTransaction.getId(), id);
        assertEquals(newTransaction.getCount(), count);
        assertEquals(newTransaction.getDate(), date);
    }
    @Test
    public void checkInsertTransactions()
    {
        int count = nextInt(100) + 100;
        for(int i=0; i<count; i++)
        {
            long date = Integer.MAX_VALUE;
            date += nextInt();
            transactions.add(new Transaction(nextInt(), nextInt(), date));
        }
        assertEquals("listModel size != "+count+" after add", transactions.getAll().size(), count);
    }
    @Test
    public void checkClearTransactions()
    {
        int count = nextInt(100) + 100;
        for(int i=0; i<count; i++)
        {
            long date = Integer.MAX_VALUE;
            date += nextInt();
            transactions.add(new Transaction(nextInt(), nextInt(), date));
        }
        transactions.clear();
        assertEquals("listModel size != "+0+" after clear", transactions.getAll().size(), 0);
    }
    @Test
    public void checkGetTransaction()
    {
        int count = nextInt();
        long date = Integer.MAX_VALUE;
        date += nextInt();
        int id = nextInt();
        transactions.add(new Transaction(
                id
                ,count
                ,date
        ));
        TransactionModel transaction = transactions.get(id);
        assertNotNull(transaction);
        assertEquals(transaction.getId(), id);
        assertEquals(transaction.getCount(), count);
        assertEquals(transaction.getDate(), date);
    }
}