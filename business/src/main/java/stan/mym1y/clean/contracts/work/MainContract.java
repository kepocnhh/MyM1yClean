package stan.mym1y.clean.contracts.work;

import java.util.List;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.data.Pair;

public interface MainContract
{
    interface Model
    {
        List<Pair<Transaction, Transaction.Extra>> getAllTransactions();
        List<Pair<CashAccount, CashAccount.Extra>> getAllCashAccounts();
        void updateAll()
                throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
        void sendAllUpdatings()
                throws ErrorsContract.NetworkException, ErrorsContract.UnknownException, ErrorsContract.UnauthorizedException;
        void sendUpdatingsCashAccount(long cashAccountId)
                throws ErrorsContract.NetworkException, ErrorsContract.UnknownException, ErrorsContract.UnauthorizedException;
        CashAccount.Extra getBalance(Currency currency);
        void delete(CashAccount cashAccount);
        void delete(Transaction transaction);
        CashAccount add(CashAccountViewModel cashAccount);
        Transaction add(TransactionViewModel transaction);
        void sync();
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error();
        void emptyCashAccounts();
        void emptyTransactions(List<Pair<CashAccount, CashAccount.Extra>> cashAccounts);
        void update(List<Pair<CashAccount, CashAccount.Extra>> cashAccounts, List<Pair<Transaction, Transaction.Extra>> transactions);
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