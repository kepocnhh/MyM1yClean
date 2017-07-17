package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.List;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.Scheduler;
import stan.reactive.Tuple;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.single.SingleObserver;

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
        model().add(cashAccount)
               .subscribeOn(Scheduler.NEW)
               .observeOn(Scheduler.NEW)
               .subscribe(new SingleObserver.Just<CashAccount>()
               {
                   public void success(CashAccount cashAccount)
                   {
                       updateCashAccount(cashAccount.id());
                   }
               });
    }
    public void add(final TransactionViewModel transaction)
    {
        model().add(transaction)
               .subscribeOn(Scheduler.NEW)
               .observeOn(Scheduler.NEW)
               .subscribe(new SingleObserver.Just<Transaction>()
               {
                   public void success(Transaction transaction)
                   {
                       updateCashAccount(transaction.cashAccountId());
                   }
               });
    }
    public void delete(final CashAccount cashAccount)
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().delete(cashAccount);
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
        List<Tuple<CashAccount, CashAccount.Extra>> cashAccounts = model().getAllCashAccounts();
        List<Tuple<Transaction, Transaction.Extra>> transactions = model().getAllTransactions();
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
            checkBalance(cashAccounts);
        }
    }
    private void checkBalance(List<Tuple<CashAccount, CashAccount.Extra>> cashAccounts)
    {
        List<Currency> currencies = new ArrayList<>();
        currencies.add(cashAccounts.get(0).second().currency());
        for(Tuple<CashAccount, CashAccount.Extra> tuple : cashAccounts)
        {
            boolean find = false;
            for(Currency currency : currencies)
            {
                if(tuple.second().currency().codeName().equals(currency.codeName()))
                {
                    find = true;
                    break;
                }
            }
            if(!find)
            {
                currencies.add(tuple.second().currency());
            }
        }
        if(currencies.size() == 1)
        {
            view().update(model().getBalance(currencies.get(0)));
        }
        else
        {
            List<CashAccount.Extra> extras = new ArrayList<>(currencies.size());
            for(Currency currency : currencies)
            {
                extras.add(model().getBalance(currency));
            }
            view().update(extras);
        }
    }
    private void updateCashAccount(final long cashAccountId)
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
                model().sendUpdatingsCashAccount(cashAccountId).subscribe(sendUpdatingsObserver);
            }
        });
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
                model().sendAllUpdatings().subscribe(sendUpdatingsObserver);
            }
        });
    }
}