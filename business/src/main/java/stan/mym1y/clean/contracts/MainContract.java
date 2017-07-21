package stan.mym1y.clean.contracts;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.reactive.Tuple;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public interface MainContract
{
    interface Model
    {
        List<Tuple<Transaction, Transaction.Extra>> getAllTransactions();
        List<Tuple<CashAccount, CashAccount.Extra>> getAllCashAccounts();
        NotifyObservable updateAll();
        NotifyObservable sendAllUpdatings();
        NotifyObservable sendUpdatingsCashAccount(long cashAccountId);
        CashAccount.Extra getBalance(Currency currency);
        void delete(CashAccount cashAccount);
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
        void emptyTransactions(List<Tuple<CashAccount, CashAccount.Extra>> cashAccounts);
        void update(List<Tuple<CashAccount, CashAccount.Extra>> cashAccounts, List<Tuple<Transaction, Transaction.Extra>> transactions);
        void update(CashAccount.Extra balance);
        void update(List<CashAccount.Extra> balance);
    }
    interface Presenter
    {
        void update();
        void add(CashAccountViewModel cashAccount);
        void add(TransactionViewModel transaction);
        void delete(CashAccount cashAccount);
        void delete(Transaction transaction);
    }

    interface Behaviour
    {
        void unauthorized();
    }
}