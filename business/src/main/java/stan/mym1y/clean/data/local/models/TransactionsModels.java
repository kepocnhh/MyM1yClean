package stan.mym1y.clean.data.local.models;

import java.util.List;

import stan.mym1y.clean.cores.transactions.Transaction;

public interface TransactionsModels
{
    interface Transactions
    {
        List<Transaction> getAll();
        List<Transaction> getAllFromCashAccountId(long cashAccountId);
        Transaction get(long id);
        void remove(long id);
        void removeAllFromCashAccountId(long cashAccountId);
        void add(Transaction transaction);
        void addAll(List<Transaction> transactions);
        void clear();
    }
}