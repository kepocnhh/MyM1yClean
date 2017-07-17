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
                map.put("id", data.id());
                map.put("cashAccountId", data.cashAccountId());
                map.put("count", data.count());
                map.put("minorCount", data.minorCount());
                map.put("date", data.date());
                map.put("income", data.income());
                return map;
            }
            public Transaction read(Map map)
            {
                return new TransactionData((Long)map.get("id"),
                        (Long)map.get("cashAccountId"),
                        (Long)map.get("date"),
                        (Boolean)map.get("income"),
                        ((Long)map.get("count")).intValue(),
                        ((Long)map.get("minorCount")).intValue());
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
                        return t1.date() < t2.date() ? 1 : t1.date() == t2.date() ? 0 : -1;
                    }
                });
            }
            public List<Transaction> getAllFromCashAccountId(final long cashAccountId)
            {
                return transactionsBox.get(new Query<Transaction>()
                {
                    public boolean query(Transaction transaction)
                    {
                        return transaction.cashAccountId() == cashAccountId;
                    }
                }, new Comparator<Transaction>()
                {
                    public int compare(Transaction t1, Transaction t2)
                    {
                        return t1.date() < t2.date() ? 1 : t1.date() == t2.date() ? 0 : -1;
                    }
                });
            }
            public Transaction get(final long id)
            {
                List<Transaction> transactions = transactionsBox.get(new Query<Transaction>()
                {
                    public boolean query(Transaction transaction)
                    {
                        return transaction.id() == id;
                    }
                });
                return !transactions.isEmpty() ? transactions.get(0) : null;
            }
            public void remove(final long id)
            {
                transactionsBox.removeFirst(new Query<Transaction>()
                {
                    public boolean query(Transaction transaction)
                    {
                        return transaction.id() == id;
                    }
                });
            }
            public void removeAllFromCashAccountId(final long cashAccountId)
            {
                transactionsBox.removeAll(new Query<Transaction>()
                {
                    public boolean query(Transaction transaction)
                    {
                        return transaction.cashAccountId() == cashAccountId;
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