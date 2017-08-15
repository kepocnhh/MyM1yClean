package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.data.Pair;
import stan.mym1y.clean.modules.data.PairData;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class AddNewTransactionPresenter
    extends ModelPresenter<AddNewTransactionContract.View, AddNewTransactionContract.Model>
    implements AddNewTransactionContract.Presenter
{
    AddNewTransactionPresenter(AddNewTransactionContract.View v, AddNewTransactionContract.Model m)
    {
        super(v, m);
    }

    public void update()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                view().cashAccounts(model().getCashAccounts());
            }
        });
    }

    public void setCashAccount(CashAccount cashAccount)
    {
        if(cashAccount.id() != model().getNewTransaction().cashAccountId())
        {
            model().setCashAccount(cashAccount);
            model().setCount(true, 0, 0);
            view().updateTransaction(model().getNewTransaction(), model().getCurrency());
        }
    }
    public void setCount(boolean income, int count, int minorCount)
    {
        model().setCount(income ,count, minorCount);
        view().updateTransaction(model().getNewTransaction(), model().getCurrency());
    }
    public void setDate(long date)
    {
        model().setDate(date);
    }
    public Pair<TransactionViewModel, Currency> updateTransaction()
    {
        return PairData.create(model().getNewTransaction(), model().getCurrency());
    }
    public void addNewTransaction()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().checkNewTransaction();
                    view().addNewTransaction(model().getNewTransaction());
                }
                catch(AddNewTransactionContract.ValidateDataException e)
                {
                    view().error(e);
                }
            }
        });
    }
}
