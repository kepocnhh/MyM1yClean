package stan.mym1y.clean.contracts;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.reactive.Tuple;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public interface MainContract
{
    interface Model
    {
        List<Tuple<CashAccount, Transaction>> getAllTransactions();
        List<CashAccount> getAllCashAccounts();
        NotifyObservable updateAll();
        NotifyObservable sendAllUpdatings();
        NotifyObservable sendUpdatingsCashAccount(long cashAccountId);
        int getBalance();
        void delete(Transaction transaction);
        SingleObservable<CashAccount> add(CashAccountViewModel cashAccount);
        SingleObservable<Transaction> add(TransactionViewModel transaction);
        void sync();
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error();
        void emptyCashAccounts();
        void emptyTransactions(List<CashAccount> cashAccounts);
        void update(List<CashAccount> cashAccounts, List<Tuple<CashAccount, Transaction>> transactions);
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