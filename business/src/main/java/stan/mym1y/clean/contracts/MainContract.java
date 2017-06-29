package stan.mym1y.clean.contracts;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
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
        void delete(Transaction transaction);
        void add(CashAccountViewModel cashAccount);
        void add(TransactionViewModel transaction);
        void sync();
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error();
        void emptyCashAccounts();
        void emptyTransactions(List<CashAccount> cashAccounts);
        void update(List<CashAccount> cashAccounts, List<Transaction> transactions);
        void update(int balance);
    }
    interface Presenter
    {
        void update();
        void add(CashAccountViewModel cashAccount);
        void add(TransactionViewModel transaction);
        void delete(Transaction transaction);
    }

    interface Behaviour
    {
        void logout();
    }
}