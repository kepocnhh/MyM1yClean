package stan.mym1y.clean.modules.main;

import java.util.List;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.notify.NotifyObserver;

class MainPresenter
    extends ModelPresenter<MainContract.View, MainContract.Model>
    implements MainContract.Presenter
{
    private final NotifyObserver sendUpdatingsObserver = new NotifyObserver()
    {
        public void notice()
        {
            log("send updatings success");
        }
        public void error(Throwable t)
        {
            if(t instanceof ErrorsContract.UnauthorizedException)
            {
                try
                {
                    throw t;
                }
                catch(ErrorsContract.UnauthorizedException e)
                {
                    view().error(e);
                }
                catch(ErrorsContract.NetworkException e)
                {
                    view().error(e);
                }
                catch(Throwable throwable)
                {
                    view().error();
                }
            }
        }
    };

    MainPresenter(MainContract.View v, MainContract.Model m)
    {
        super(v, m);
    }

    public void update()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                updateLocal();
                model().updateAll().subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        log("update all success");
                        updateLocal();
                    }
                    public void error(Throwable t)
                    {
//                        log("error " + t.toString());
                        try
                        {
                            throw t;
                        }
                        catch(ErrorsContract.DataNotExistException e)
                        {
                            log("data still not exist");
                            model().sync();
                            updateAll();
                        }
                        catch(ErrorsContract.UnauthorizedException e)
                        {
                            view().error(e);
                        }
                        catch(ErrorsContract.NetworkException e)
                        {
                            view().error(e);
                        }
                        catch(Throwable throwable)
                        {
                            view().error();
                        }
                    }
                });
            }
        });
    }

    public void add(final CashAccountViewModel cashAccount)
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().add(cashAccount);
                updateAll();
            }
        });
    }
    public void add(final TransactionViewModel transaction)
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().add(transaction);
                updateAll();
            }
        });
    }

    public void delete(final Transaction transaction)
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().delete(transaction);
                updateAll();
            }
        });
    }

    private void updateLocal()
    {
        List<CashAccount> cashAccounts = model().getAllCashAccounts();
        List<Transaction> transactions = model().getAllTransactions();
        if(cashAccounts.isEmpty())
        {
            view().emptyCashAccounts();
        }
        else if(transactions.isEmpty())
        {
            view().emptyTransactions(cashAccounts);
        }
        else
        {
            view().update(cashAccounts, transactions);
        }
        view().update(model().getBalance());
    }
    private void updateAll()
    {
        together(new Runnable()
        {
            public void run()
            {
                updateLocal();
            }
        }, new Runnable()
        {
            public void run()
            {
                model().sendUpdatings().subscribe(sendUpdatingsObserver);
            }
        });
    }
}