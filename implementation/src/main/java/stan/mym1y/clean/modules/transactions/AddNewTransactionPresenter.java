package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
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
        model().setCashAccount(cashAccount);
    }
    public void setCount(int count, int minorCount)
    {
        model().setCount(count, minorCount);
    }
    public void setDate(long date)
    {
        model().setDate(date);
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
                    view().addNewCashAccount(model().getNewTransaction());
                }
                catch(AddNewTransactionContract.ValidateDataException e)
                {
                    view().error(e);
                }
            }
        });
    }
}
