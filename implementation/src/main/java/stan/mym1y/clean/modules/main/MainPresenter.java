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
    private final Observer<ListModel<TransactionModel>> transactionsCacheObserver = new JustObserver<ListModel<TransactionModel>>()
    {
        public void next(ListModel<TransactionModel> t)
        {
            getView().update(t);
        }
    };
    private final Observer<Integer> balanceObserver = new JustObserver<Integer>()
    {
        public void next(Integer balance)
        {
            getView().update(balance);
        }
    };
    private final Observer<ListModel<TransactionModel>> transactionsUpdateObserver = new Observer<ListModel<TransactionModel>>()
    {
        @Override
        public void next(ListModel<TransactionModel> transactions)
        {
            log("update transactions success: " + transactions.size());
            getModel().getBalance().subscribe(balanceObserver);
            getView().update(transactions);
        }
        @Override
        public void error(Throwable t)
        {
            log("update transactions error: " + t.getMessage());
            try
            {
                throw t;
            }
            catch(ErrorsContract.NetworkErrorException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.UnauthorizedException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.InvalidDataException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.ServerErrorException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.UnknownErrorException exception)
            {
                getView().error(exception);
            }
            catch(Throwable throwable)
            {
                getView().error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nerror " + t.getMessage()));
            }
        }
        @Override
        public void complete()
        {
        }
    };
    private final Observer<ListModel<TransactionModel>> sendUpdatingsObserver = new Observer<ListModel<TransactionModel>>()
    {
        @Override
        public void next(ListModel<TransactionModel> transactions)
        {
            log("send updatings success: " + transactions.size());
        }
        @Override
        public void error(Throwable t)
        {
            log("send updatings error: " + t.getMessage());
        }
        @Override
        public void complete()
        {
        }
    };

    MainPresenter(MainContract.View v, MainContract.Model m)
    {
        super(v, m);
    }

    @Override
    public void update()
    {
        together(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().getAll().subscribe(transactionsCacheObserver);
                getModel().getBalance().subscribe(balanceObserver);
            }
        }, new Runnable()
        {
            @Override
            public void run()
            {
                getModel().updateAll().subscribe(transactionsUpdateObserver);
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
                together(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getModel().getAll().subscribe(transactionsCacheObserver);
                        getModel().getBalance().subscribe(balanceObserver);
                    }
                }, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getModel().sendUpdatings().subscribe(sendUpdatingsObserver);
                    }
                });
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
                together(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getModel().getAll().subscribe(transactionsCacheObserver);
                        getModel().getBalance().subscribe(balanceObserver);
                    }
                }, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getModel().sendUpdatings().subscribe(sendUpdatingsObserver);
                    }
                });
            }
        });
    }
}