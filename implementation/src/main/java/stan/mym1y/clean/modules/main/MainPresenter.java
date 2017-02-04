package stan.mym1y.clean.modules.main;

import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class MainPresenter
    extends ModelPresenter<MainContract.View, MainContract.Model>
    implements MainContract.Presenter
{
    MainPresenter(MainContract.View v, MainContract.Model m)
    {
        super(v, m);
    }

    @Override
    public void update()
    {
        runOnNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getView().update(getModel().getAll());
                getView().update(getModel().getBalance());
            }
        });
    }

    @Override
    public void newTransaction(final TransactionModel transaction)
    {
        runOnNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().add(transaction);
                getView().update(getModel().getAll());
                getView().update(getModel().getBalance());
            }
        });
    }
}