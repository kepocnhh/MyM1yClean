package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.dao.ListModel;

public interface MainContract
{
    interface Model
    {
        ListModel<TransactionModel> getAll(int sortingType);
        int getBalance();
        void add(TransactionModel transaction);
        void delete(int id);
    }
    interface View
    {
        void update(ListModel<TransactionModel> transactions);
        void update(int balance);
    }
    interface Presenter
    {
        void update();
        void changeSorting();
        void newTransaction(TransactionViewModel transaction);
        void deleteTransaction(int id);
    }
}