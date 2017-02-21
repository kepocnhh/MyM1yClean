package stan.mym1y.clean.dao;

import java.util.Collection;

import stan.mym1y.clean.cores.transactions.TransactionModel;

public interface Models
{
    interface Transactions
    {
        ListModel<TransactionModel> getAll();
        ListModel<TransactionModel> getAllWithSort(int sortingType);
        TransactionModel get(int id);
        void remove(int id);
        void add(TransactionModel transaction);
        void add(Collection<TransactionModel> transactions);
        void clear();
    }
}