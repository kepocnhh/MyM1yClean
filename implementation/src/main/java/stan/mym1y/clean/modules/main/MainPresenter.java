package stan.mym1y.clean.modules.main;

import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class MainPresenter
    extends ModelPresenter<MainContract.View, MainContract.Model>
    implements MainContract.Presenter
{
    private final Settings settings;
    private int sortingType;

    MainPresenter(MainContract.View v, MainContract.Model m, Settings ss)
    {
        super(v, m);
        settings = ss;
    }

    @Override
    public void update()
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                sortingType = settings.getSortingType();
                if(sortingType == -1)
                {
                    sortingType = 1;
                    settings.setSortyngType(sortingType);
                }
                getView().update(getModel().getAll(sortingType));
                getView().update(getModel().getBalance());
            }
        });
    }

    @Override
    public void changeSorting()
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                sortingType = sortingType == 1 ? 0 : 1;
                log("sortingType: " + sortingType);
                settings.setSortyngType(sortingType);
                getView().update(getModel().getAll(sortingType));
                getView().update(getModel().getBalance());
            }
        });
    }

    @Override
    public void newTransaction(final TransactionViewModel transaction)
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().add(new Transaction(nextInt(), transaction.getCount(), transaction.getDate()));
                getView().update(getModel().getAll(sortingType));
                getView().update(getModel().getBalance());
            }
        });
    }

    @Override
    public void deleteTransaction(final int id)
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().delete(id);
                getView().update(getModel().getAll(sortingType));
                getView().update(getModel().getBalance());
            }
        });
    }
}