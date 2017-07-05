package stan.mym1y.clean.contracts.cashaccounts;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;

public interface AddNewCashAccountContract
{
    interface Model
    {
        List<Currency> getCurrencies();
        void setTitle(String title);
        void setCurrency(Currency currency);
        void checkNewCashAccount() throws ValidateDataException;
        CashAccountViewModel getNewCashAccount();
    }
    interface View
    {
        void currencies(List<Currency> currencies);
        void addNewCashAccount(CashAccountViewModel cashAccountViewModel);
        void error(ValidateDataException exception);
    }
    interface Presenter
    {
        void update();
        void setTitle(String title);
        void setCurrency(Currency currency);
        void addNewCashAccount();
    }

    interface Behaviour
    {
        void newCashAccount(CashAccountViewModel cashAccountViewModel);
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
            EMPTY_TITLE,
        }
    }
}