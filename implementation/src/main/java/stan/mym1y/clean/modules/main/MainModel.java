package stan.mym1y.clean.modules.main;

import java.util.List;

import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.components.Security;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.local.models.TransactionsModels;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.DataApi;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.TransactionData;
import stan.reactive.Func;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

class MainModel
    implements MainContract.Model
{
    private final TransactionsModels.Transactions transactions;
    private final Security security;
    private final Settings settings;
    private final JsonConverter jsonConverter;
    private final AuthApi authApi;
    private final DataApi dataApi;

    MainModel(TransactionsModels.Transactions t, Security sm, Settings ss, JsonConverter j, AuthApi a, DataApi d)
    {
        transactions = t;
        security = sm;
        settings = ss;
        jsonConverter = j;
        authApi = a;
        dataApi = d;
    }

    public List<Transaction> getAll()
    {
        return transactions.getAll();
    }

    public NotifyObservable updateAll()
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                dataApi.getSyncData(settings.getUserPrivateData()).subscribe(new SingleObserver<SyncData>()
                {
                    public void success(SyncData data)
                    {
                        if(data.getLastSyncTime() > settings.getSyncData().getLastSyncTime())
                        {
                            dataApi.getTransactions(settings.getUserPrivateData()).subscribe(new SingleObserver<List<Transaction>>()
                            {
                                public void success(List<Transaction> ts)
                                {
                                    transactions.clear();
                                    transactions.addAll(ts);
                                    o.notice();
                                }
                                public void error(Throwable t)
                                {
                                    o.error(t);
                                }
                            });
                        }
                        else if(data.getLastSyncTime() == settings.getSyncData().getLastSyncTime() && data.getHash().equals(settings.getSyncData().getHash()))
                        {
                            o.notice();
                        }
                        else
                        {
                            dataApi.putTransactions(settings.getUserPrivateData(), transactions.getAll()).subscribe(new NotifyObserver()
                            {
                                public void notice()
                                {
                                    o.notice();
                                }
                                public void error(Throwable t)
                                {
                                    o.error(t);
                                }
                            });
                        }
                    }
                    public void error(Throwable t)
                    {
                        if(t instanceof ErrorsContract.UnauthorizedException)
                        {
                            authApi.postRefreshToken(settings.getUserPrivateData().getRefreshToken()).subscribe(new SingleObserver<UserPrivateData>()
                            {
                                public void success(UserPrivateData data)
                                {
                                    settings.login(data);
                                    dataApi.getSyncData(settings.getUserPrivateData()).subscribe(new SingleObserver<SyncData>()
                                    {
                                        public void success(SyncData data)
                                        {
                                            if(data.getLastSyncTime() > settings.getSyncData().getLastSyncTime())
                                            {
                                                dataApi.getTransactions(settings.getUserPrivateData()).subscribe(new SingleObserver<List<Transaction>>()
                                                {
                                                    public void success(List<Transaction> ts)
                                                    {
                                                        transactions.clear();
                                                        transactions.addAll(ts);
                                                        o.notice();
                                                    }
                                                    public void error(Throwable t)
                                                    {
                                                        o.error(t);
                                                    }
                                                });
                                            }
                                            else if(data.getLastSyncTime() == settings.getSyncData().getLastSyncTime() && data.getHash().equals(settings.getSyncData().getHash()))
                                            {
                                                o.notice();
                                            }
                                            else
                                            {
                                                dataApi.putTransactions(settings.getUserPrivateData(), transactions.getAll()).subscribe(new NotifyObserver()
                                                {
                                                    public void notice()
                                                    {
                                                        o.notice();
                                                    }
                                                    public void error(Throwable t)
                                                    {
                                                        o.error(t);
                                                    }
                                                });
                                            }
                                        }
                                        public void error(Throwable t)
                                        {
                                            o.error(t);
                                        }
                                    });
                                }
                                public void error(Throwable t)
                                {
                                    o.error(t);
                                }
                            });
                        }
                        else
                        {
                            o.error(t);
                        }
                    }
                });
            }
        };
    }
    public NotifyObservable sendUpdatings()
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                dataApi.putTransactions(settings.getUserPrivateData(), transactions.getAll()).chain(new NotifyObserver()
                {
                    public void notice()
                    {
                    }
                    public void error(Throwable t)
                    {
                        if(t instanceof ErrorsContract.UnauthorizedException)
                        {
                            final NotifyObserver notifyObserver = this;
                            authApi.postRefreshToken(settings.getUserPrivateData().getRefreshToken()).subscribe(new SingleObserver<UserPrivateData>()
                            {
                                public void success(UserPrivateData data)
                                {
                                    settings.login(data);
                                    dataApi.putTransactions(settings.getUserPrivateData(), transactions.getAll()).subscribe(new NotifyObserver()
                                    {
                                        public void notice()
                                        {
                                            notifyObserver.notice();
                                        }
                                        public void error(Throwable t)
                                        {
                                            o.error(t);
                                        }
                                    });
                                }
                                public void error(Throwable t)
                                {
                                    o.error(t);
                                }
                            });
                        }
                        else
                        {
                            o.error(t);
                        }
                    }
                }, dataApi.putSyncData(settings.getUserPrivateData(), settings.getSyncData())).subscribe(o);
            }
        };
    }
    public int getBalance()
    {
        int b = 0;
        for(Transaction t : transactions.getAll())
        {
            b += t.getCount();
        }
        return b;
    }
    public void delete(int id)
    {
        transactions.remove(id);
        settings.setSyncData(new SynchronizationData(System.currentTimeMillis(),security.sha512(jsonConverter.get(transactions.getAll()))));
    }
    public void add(TransactionViewModel transaction)
    {
        transactions.add(new TransactionData(security.newUniqueId(), transaction.getCount(), transaction.getDate()));
        settings.setSyncData(new SynchronizationData(System.currentTimeMillis(),security.sha512(jsonConverter.get(transactions.getAll()))));
    }
}