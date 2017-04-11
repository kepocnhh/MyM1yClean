package stan.mym1y.clean.boxes.access;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.boxes.Box;
import stan.boxes.ORM;
import stan.boxes.Query;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.data.local.access.TransactionsAccess;
import stan.mym1y.clean.data.local.models.TransactionsModels;
import stan.mym1y.clean.modules.transactions.TransactionData;

public class Transactions
    implements TransactionsAccess
{
    private final Box<Transaction> transactionsBox;

    public Transactions(String path)
    {
        transactionsBox = new Box<>(new ORM<Transaction>()
        {
            public Map write(Transaction data)
            {
                Map map = new HashMap();
                map.put("id", data.getId());
                map.put("count", data.getCount());
                map.put("date", data.getDate());
                return map;
            }
            public Transaction read(Map map)
            {
                return new TransactionData(
                        ((Long)map.get("id")).intValue()
                        ,((Long)map.get("count")).intValue()
                        ,(Long)map.get("date")
                );
            }
        }, path + "/transactionsbox");
    }

    public TransactionsModels.Transactions transactions()
    {
        return new TransactionsModels.Transactions()
        {
            public List<Transaction> getAll()
            {
                return transactionsBox.get(new Comparator<Transaction>()
                {
                    public int compare(Transaction t1, Transaction t2)
                    {
                        return t1.getDate() < t2.getDate() ? 1 : t1.getDate() == t2.getDate() ? 0 : -1;
                    }
                });
            }
            public Transaction get(final int id)
            {
                List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
                {
                    public boolean query(Transaction transaction)
                    {
                        return transaction.getId() == id;
                    }
                });
                return transactions.size() > 0 ? transactions.get(0) : null;
            }
            public void remove(final int id)
            {
                transactionsBox.removeFirst(new Query<Transaction>()
                {
                    public boolean query(Transaction transaction)
                    {
                        return transaction.getId() == id;
                    }
                });
            }
            public void add(Transaction transaction)
            {
                transactionsBox.add(transaction);
            }
            public void addAll(List<Transaction> transactions)
            {
                transactionsBox.add(transactions);
            }
            public void clear()
            {
                transactionsBox.clear();
            }
        };
    }
}