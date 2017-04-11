package stan.mym1y.clean.data.local.models;

import java.util.List;

import stan.mym1y.clean.cores.transactions.Transaction;

public interface TransactionsModels
{
    interface Transactions
    {
        List<Transaction> getAll();
        Transaction get(int id);
        void remove(int id);
        void add(Transaction transaction);
        void addAll(List<Transaction> transactions);
        void clear();
    }
}