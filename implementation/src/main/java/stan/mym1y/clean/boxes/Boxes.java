package stan.mym1y.clean.boxes;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.boxes.Box;
import stan.boxes.ORM;
import stan.boxes.Query;
import stan.mym1y.clean.units.models.ArrayListModel;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.modules.transactions.Transaction;

public class Boxes
        implements DAO
{
    private final Box<TransactionModel> transactionsBox;
    private final Comparator<TransactionModel> transactionModelDateUpComparator = new Comparator<TransactionModel>()
    {
        @Override
        public int compare(TransactionModel t1, TransactionModel t2)
        {
            return t1.getDate() < t2.getDate() ? -1 : t1.getDate() > t2.getDate() ? 1 : 0;
        }
    };
    private final Comparator<TransactionModel> transactionModelDateDownComparator = new Comparator<TransactionModel>()
    {
        @Override
        public int compare(TransactionModel t1, TransactionModel t2)
        {
            return t1.getDate() > t2.getDate() ? -1 : t1.getDate() < t2.getDate() ? 1 : 0;
        }
    };
    private final Models.Transactions transactions = new Models.Transactions()
    {
        @Override
        public ListModel<TransactionModel> getAll()
        {
            return new ArrayListModel<>(transactionsBox.getAll());
        }
        @Override
        public ListModel<TransactionModel> getAllWithSort(int sortingType)
        {
            return new ArrayListModel<>(transactionsBox.get(sortingType == 1 ? transactionModelDateUpComparator : transactionModelDateDownComparator));
        }
        @Override
        public TransactionModel get(final int id)
        {
            List<TransactionModel> transactions = transactionsBox.get(new Query<TransactionModel>()
            {
                public boolean query(TransactionModel transaction)
                {
                    return transaction.getId() == id;
                }
            });
            return transactions.size() > 0 ? transactions.get(0) : null;
        }
        @Override
        public void remove(final int id)
        {
            transactionsBox.removeFirst(new Query<TransactionModel>()
            {
                @Override
                public boolean query(TransactionModel transactionModel)
                {
                    return transactionModel.getId() == id;
                }
            });
        }
        @Override
        public void add(TransactionModel transaction)
        {
            transactionsBox.add(transaction);
        }
        @Override
        public void clear()
        {
            transactionsBox.clear();
        }
    };

    public Boxes(String path)
    {
        transactionsBox = new Box<>(new ORM<TransactionModel>()
        {
            public Map write(TransactionModel data)
            {
                Map map = new HashMap();
                map.put("id", data.getId());
                map.put("count", data.getCount());
                map.put("date", data.getDate());
                return map;
            }
            public TransactionModel read(Map map)
            {
                return new Transaction(
                        Long.valueOf((Long)map.get("id")).intValue()
                        ,Long.valueOf((Long)map.get("count")).intValue()
                        ,(Long)map.get("date")
                );
            }
        }, path + "/transactionsbox");
    }
    
    @Override
    public Models.Transactions getTransactions()
    {
        return transactions;
    }
}