package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.ListModel;

public interface MainContract
{
    interface Model
    {
        ListModel<TransactionModel> getAll();
        int getBalance();
        void add(TransactionModel transaction);
    }
    interface View
    {
        void update(ListModel<TransactionModel> transactions);
        void update(int balance);
    }
    interface Presenter
    {
        void update();
        void newTransaction(TransactionModel transaction);
    }
}