package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.List;

import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.components.Security;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.data.local.models.TransactionsModels;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.DataApi;
import stan.mym1y.clean.modules.cashaccounts.CashAccountData;
import stan.mym1y.clean.modules.network.requests.CashAccountRequestData;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.TransactionData;
import stan.reactive.Tuple;
import stan.reactive.functions.Apply;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

class MainModel
    implements MainContract.Model
{
    private final TransactionsModels.Transactions transactions;
    private final CashAccountsModels.CashAccounts cashAccounts;
    private final Security security;
    private final Settings settings;
    private final JsonConverter jsonConverter;
    private final AuthApi authApi;
    private final DataApi dataApi;

    MainModel(TransactionsModels.Transactions ts, CashAccountsModels.CashAccounts cas, Security sm, Settings ss, JsonConverter j, AuthApi a, DataApi d)
    {
        transactions = ts;
        cashAccounts = cas;
        security = sm;
        settings = ss;
        jsonConverter = j;
        authApi = a;
        dataApi = d;
    }

    public List<Tuple<CashAccount, Transaction>> getAllTransactions()
    {
        List<Transaction> ts = transactions.getAll();
        List<Tuple<CashAccount, Transaction>> list = new ArrayList<>(ts.size());
        for(Transaction transaction: ts)
        {
            list.add(new FullTransaction(cashAccounts.get(transaction.cashAccountId()), transaction));
        }
        return list;
    }
    private class FullTransaction
        implements Tuple<CashAccount, Transaction>
    {
        private final CashAccount cashAccount;
        private final Transaction transaction;

        FullTransaction(CashAccount cashAccount, Transaction transaction)
        {
            this.cashAccount = cashAccount;
            this.transaction = transaction;
        }

        public CashAccount first()
        {
            return cashAccount;
        }
        public Transaction second()
        {
            return transaction;
        }
    }

    public List<CashAccount> getAllCashAccounts()
    {
        return cashAccounts.getAll();
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
                        if(data.lastSyncTime() > settings.getSyncData().lastSyncTime())
                        {
                            dataApi.getTransactions(settings.getUserPrivateData()).subscribe(new SingleObserver<List<CashAccountRequest>>()
                            {
                                public void success(List<CashAccountRequest> cashAccountRequests)
                                {
                                    cashAccounts.clear();
                                    transactions.clear();
                                    for(CashAccountRequest cashAccountRequest: cashAccountRequests)
                                    {
                                        cashAccounts.add(cashAccountRequest.cashAccount());
                                        transactions.addAll(cashAccountRequest.transactions());
                                    }
                                    o.notice();
                                }
                                public void error(Throwable t)
                                {
                                    o.error(t);
                                }
                            });
                        }
                        else if(data.lastSyncTime() == settings.getSyncData().lastSyncTime() && data.hash().equals(settings.getSyncData().hash()))
                        {
                            o.notice();
                        }
                        else
                        {
                            dataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests()).subscribe(new NotifyObserver()
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
                            authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()).subscribe(new SingleObserver<UserPrivateData>()
                            {
                                public void success(UserPrivateData data)
                                {
                                    settings.login(data);
                                    dataApi.getSyncData(settings.getUserPrivateData()).subscribe(new SingleObserver<SyncData>()
                                    {
                                        public void success(SyncData data)
                                        {
                                            if(data.lastSyncTime() > settings.getSyncData().lastSyncTime())
                                            {
                                                dataApi.getTransactions(settings.getUserPrivateData()).subscribe(new SingleObserver<List<CashAccountRequest>>()
                                                {
                                                    public void success(List<CashAccountRequest> cashAccountRequests)
                                                    {
                                                        cashAccounts.clear();
                                                        transactions.clear();
                                                        for(CashAccountRequest cashAccountRequest: cashAccountRequests)
                                                        {
                                                            cashAccounts.add(cashAccountRequest.cashAccount());
                                                            transactions.addAll(cashAccountRequest.transactions());
                                                        }
                                                        o.notice();
                                                    }
                                                    public void error(Throwable t)
                                                    {
                                                        o.error(t);
                                                    }
                                                });
                                            }
                                            else if(data.lastSyncTime() == settings.getSyncData().lastSyncTime() && data.hash().equals(settings.getSyncData().hash()))
                                            {
                                                o.notice();
                                            }
                                            else
                                            {
                                                dataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests()).subscribe(new NotifyObserver()
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
    public NotifyObservable sendAllUpdatings()
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                dataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests()).chain(new NotifyObserver()
                {
                    public void notice()
                    {
                    }
                    public void error(Throwable t)
                    {
                        if(t instanceof ErrorsContract.UnauthorizedException)
                        {
                            final NotifyObserver notifyObserver = this;
                            authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()).subscribe(new SingleObserver<UserPrivateData>()
                            {
                                public void success(UserPrivateData data)
                                {
                                    settings.login(data);
                                    dataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequests()).subscribe(new NotifyObserver()
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
    public NotifyObservable sendUpdatingsCashAccount(final long cashAccountId)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                dataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequest(cashAccounts.get(cashAccountId))).chain(new NotifyObserver()
                {
                    public void notice()
                    {
                    }
                    public void error(Throwable t)
                    {
                        if(t instanceof ErrorsContract.UnauthorizedException)
                        {
                            final NotifyObserver notifyObserver = this;
                            authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()).subscribe(new SingleObserver<UserPrivateData>()
                            {
                                public void success(UserPrivateData data)
                                {
                                    settings.login(data);
                                    dataApi.putTransactions(settings.getUserPrivateData(), getCashAccountRequest(cashAccounts.get(cashAccountId))).subscribe(new NotifyObserver()
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
    private CashAccountRequest getCashAccountRequest(CashAccount cashAccount)
    {
        return new CashAccountRequestData(cashAccount, transactions.getAllFromCashAccountId(cashAccount.id()));
    }
    private List<CashAccountRequest> getCashAccountRequests()
    {
        List<CashAccountRequest> list = new ArrayList<>();
        for(CashAccount cashAccount: cashAccounts.getAll())
        {
            list.add(getCashAccountRequest(cashAccount));
        }
        return list;
    }
    public int getBalance()
    {
        int b = 0;
        for(Transaction t : transactions.getAll())
        {
            b += t.count();
        }
        return b;
    }
    public void delete(Transaction transaction)
    {
        transactions.remove(transaction.id());
        updateSyncData();
    }

    public SingleObservable<CashAccount> add(final CashAccountViewModel cashAccount)
    {
        return SingleObservable.create(new Apply<CashAccount>()
        {
            public CashAccount apply()
            {
                long id = security.newUniqueId();
                cashAccounts.add(new CashAccountData(id, cashAccount.title()));
                updateSyncData();
                return cashAccounts.get(id);
            }
        });
    }
    public SingleObservable<Transaction> add(final TransactionViewModel transaction)
    {
        return SingleObservable.create(new Apply<Transaction>()
        {
            public Transaction apply()
            {
                long id = security.newUniqueId();
                transactions.add(new TransactionData(id, transaction.cashAccountId(), transaction.date(), transaction.count()));
                updateSyncData();
                return transactions.get(id);
            }
        });
    }
    public void sync()
    {
        updateSyncData();
    }

    private void updateSyncData()
    {
        List<Transaction> list = transactions.getAll();
        settings.setSyncData(new SynchronizationData(System.currentTimeMillis(), security.sha512(System.currentTimeMillis() + "" + list.size() + "" + list.hashCode() + jsonConverter.get(list))));
    }
}