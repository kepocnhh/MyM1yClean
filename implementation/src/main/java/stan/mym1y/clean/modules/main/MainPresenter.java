package stan.mym1y.clean.modules.main;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.JustObserver;
import stan.reactive.Observer;

class MainPresenter
    extends ModelPresenter<MainContract.View, MainContract.Model>
    implements MainContract.Presenter
{
    private final Observer<ListModel<TransactionModel>> transactionsObserver = new Observer<ListModel<TransactionModel>>()
    {
        @Override
        public void next(ListModel<TransactionModel> transactions)
        {
            getView().update(transactions);
        }
        @Override
        public void error(Throwable t)
        {
            if(t instanceof ErrorsContract.NetworkErrorException)
            {
                getView().error((ErrorsContract.NetworkErrorException)t);
            }
            else if(t instanceof ErrorsContract.UnauthorizedException)
            {
                getView().error((ErrorsContract.UnauthorizedException)t);
            }
            else if(t instanceof ErrorsContract.InvalidDataException)
            {
                getView().error((ErrorsContract.InvalidDataException)t);
            }
            else if(t instanceof ErrorsContract.ServerErrorException)
            {
                getView().error((ErrorsContract.ServerErrorException)t);
            }
            else
            {
                getView().error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nerror " + t.getMessage()));
            }
        }
        @Override
        public void complete()
        {
        }
    };
    private final Observer<Integer> balanceObserver = new JustObserver<Integer>()
    {
        public void next(Integer balance)
        {
            getView().update(balance);
        }
    };

    MainPresenter(MainContract.View v, MainContract.Model m)
    {
        super(v, m);
    }

    @Override
    public void update()
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().getAll().subscribe(transactionsObserver);
            }
        });
//        updateAll();
//        onNewThread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                getView().update(getModel().getAll(sortingType));
//                getView().update(getModel().getBalance());
//            }
//        });
    }

    @Override
    public void newTransaction(final TransactionViewModel transaction)
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
//                getModel().add(new Transaction(nextInt(), transaction.getCount(), transaction.getDate()));
//                getView().update(getModel().getAll(sortingType));
//                getView().update(getModel().getBalance());
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
//                getModel().delete(id);
//                getView().update(getModel().getAll(sortingType));
//                getView().update(getModel().getBalance());
            }
        });
    }

    private void updateAll()
    {
        together(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().getAll().subscribe(transactionsObserver);
            }
        }, new Runnable()
        {
            @Override
            public void run()
            {
                getModel().getBalance().subscribe(balanceObserver);
            }
        });
    }
}