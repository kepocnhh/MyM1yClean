package stan.mym1y.clean.modules.cashaccounts;

import stan.mym1y.clean.contracts.cashaccounts.AddNewCashAccountContract;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class AddNewCashAccountPresenter
    extends ModelPresenter<AddNewCashAccountContract.View, AddNewCashAccountContract.Model>
    implements AddNewCashAccountContract.Presenter
{
    AddNewCashAccountPresenter(AddNewCashAccountContract.View v, AddNewCashAccountContract.Model m)
    {
        super(v, m);
    }

    public void update()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                view().currencies(model().getCurrencies());
            }
        });
    }
    public void setTitle(String title)
    {
        model().setTitle(title);
    }
    public void setCurrency(Currency currency)
    {
        model().setCurrency(currency);
    }
    public void addNewCashAccount()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().checkNewCashAccount();
                    view().addNewCashAccount(model().getNewCashAccount());
                }
                catch(AddNewCashAccountContract.ValidateDataException e)
                {
                    view().error(e);
                }
            }
        });
    }
}