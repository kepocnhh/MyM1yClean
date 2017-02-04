package stan.mym1y.clean.dao;

import stan.mym1y.clean.cores.transactions.TransactionModel;

public interface Models
{
    interface Transactions
    {
        ListModel<TransactionModel> getAll();
        TransactionModel get(int id);
        void add(TransactionModel transaction);
        void clear();
    }
}