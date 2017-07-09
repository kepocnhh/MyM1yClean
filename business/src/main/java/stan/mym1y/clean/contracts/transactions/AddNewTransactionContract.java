package stan.mym1y.clean.contracts.transactions;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.reactive.Tuple;
import stan.reactive.single.SingleObservable;

public interface AddNewTransactionContract
{
    interface Model
    {
        List<CashAccount> getCashAccounts();
        void setCashAccount(CashAccount cashAccount);
        void setCount(int count, int minorCount);
        void setDate(long date);
        void checkNewTransaction() throws ValidateDataException;
        TransactionViewModel getNewTransaction();
        Currency getCurrency();
    }
    interface View
    {
        void cashAccounts(List<CashAccount> cashAccounts);
        void addNewTransaction(TransactionViewModel transactionViewModel);
        void updateTransaction(TransactionViewModel transactionViewModel, Currency currency);
        void error(ValidateDataException exception);
    }
    interface Presenter
    {
        void update();
        SingleObservable<Tuple<TransactionViewModel, Currency>> updateTransaction();
        void setCashAccount(CashAccount cashAccount);
        void setCount(int count, int minorCount);
        void setDate(long date);
        void addNewTransaction();
    }

    interface Behaviour
    {
        void newTransaction(TransactionViewModel transactionViewModel);
        void cancel();
    }

    class ValidateDataException
            extends Exception
    {
        private final Error error;

        public ValidateDataException(Error e)
        {
            error = e;
        }

        public Error error()
        {
            return error;
        }

        public enum Error
        {
            EMPTY_COUNT,
        }
    }
}