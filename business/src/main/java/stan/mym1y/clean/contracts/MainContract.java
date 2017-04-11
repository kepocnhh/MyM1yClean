package stan.mym1y.clean.contracts;

import java.util.List;

import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.reactive.notify.NotifyObservable;

public interface MainContract
{
    interface Model
    {
        List<Transaction> getAll();
        NotifyObservable updateAll();
        NotifyObservable sendUpdatings();
        int getBalance();
        void delete(int id);
        void add(TransactionViewModel transaction);
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error();
        void update(List<Transaction> transactions);
        void update(int balance);
    }
    interface Presenter
    {
        void update();
        void newTransaction(TransactionViewModel transaction);
        void deleteTransaction(int id);
    }

    interface Behaviour
    {
        void logout();
    }
}