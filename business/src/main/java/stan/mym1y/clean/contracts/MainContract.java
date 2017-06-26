package stan.mym1y.clean.contracts;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.reactive.notify.NotifyObservable;

public interface MainContract
{
    interface Model
    {
        List<Transaction> getAllTransactions();
        List<CashAccount> getAllCashAccounts();
        NotifyObservable updateAll();
        NotifyObservable sendUpdatings();
        int getBalance();
        void delete(int id);
        void add(TransactionViewModel transaction);
        void sync();
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error();
        void updateTransactions(List<Transaction> transactions);
        void updateCashAccounts(List<CashAccount> cashAccounts);
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