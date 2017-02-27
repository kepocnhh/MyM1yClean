package stan.mym1y.clean.modules.main;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.single.SingleObserver;

class MainPresenter
    extends ModelPresenter<MainContract.View, MainContract.Model>
    implements MainContract.Presenter
{
    private final SingleObserver<ListModel<TransactionModel>> transactionsCacheObserver = new SingleObserver.Just<ListModel<TransactionModel>>()
    {
        @Override
        public void success(ListModel<TransactionModel> transactions)
        {
            getView().update(transactions);
        }
    };
    private final SingleObserver<Integer> balanceObserver = new SingleObserver.Just<Integer>()
    {
        @Override
        public void success(Integer balance)
        {
            getView().update(balance);
        }
    };
    private final SingleObserver<ListModel<TransactionModel>> transactionsUpdateObserver = new SingleObserver<ListModel<TransactionModel>>()
    {
        @Override
        public void success(ListModel<TransactionModel> transactions)
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
    };
    private final NotifyObserver sendUpdatingsObserver = new NotifyObserver()
    {
        @Override
        public void notice()
        {
            log("send updatings success");
        }
        @Override
        public void error(Throwable t)
        {
            log("send updatings error: " + t.getMessage());
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