package stan.mym1y.clean.modules.main;

import java.util.List;

import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.di.JsonConverter;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.reactive.Func;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

class MainModel
    implements MainContract.Model
{
    private final Models.Transactions transactions;
    private final Connection connection;
    private final Settings settings;
    private final JsonConverter jsonConverter;

    private final SingleObservable<ListModel<TransactionModel>> transactionsCacheObservable = new SingleObservable.Work<ListModel<TransactionModel>>()
    {
        @Override
        protected ListModel<TransactionModel> work()
                throws Throwable
        {
            return transactions.getAll();
        }
    };
    private final Func<ListModel<TransactionModel>, Integer> balanceMap = new Func<ListModel<TransactionModel>, Integer>()
    {
        public Integer call(ListModel<TransactionModel> allTransactions)
        {
            int balance = 0;
            for(int i=0; i<allTransactions.size(); i++)
            {
                balance += allTransactions.get(i).getCount();
            }
            return balance;
        }
    };
    private final SingleObservable<Integer> balanceObservable = transactionsCacheObservable.map(balanceMap);
    private abstract class NetworkObservable<DATA>
        extends SingleObservable<DATA>
    {
        SingleObserver<DATA> observer;

        @Override
        public void subscribe(SingleObserver<DATA> o)
        {
            observer = o;
            create().subscribe(new SingleObserver<Connection.Answer>()
            {
                @Override
                public void success(Connection.Answer response)
                {
                    try
                    {
                        checkResponseCode(response.getCode());
                        observer.success(work(response.getData()));
                    }
                    catch(ErrorsContract.UnauthorizedException e)
                    {
                        onError(e);
                    }
                    catch(ErrorsContract.UnknownErrorException e)
                    {
                        observer.error(e);
                    }
                    catch(Exception e)
                    {
                        observer.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nsuccess " + response));
                    }
                }
                @Override
                public void error(Throwable t)
                {
                    observer.error(new ErrorsContract.NetworkErrorException(t.getMessage()));
                }
            });
        }
        void checkResponseCode(int code)
                throws ErrorsContract.UnauthorizedException, ErrorsContract.UnknownErrorException, ErrorsContract.InvalidDataException
        {
            if(code == 401)
            {
                throw new ErrorsContract.UnauthorizedException("code: " + code);
            }
            else if(code == 400)
            {
                throw new ErrorsContract.InvalidDataException("answer: " + code);
            }
            else if(code != 200)
            {
                throw new ErrorsContract.UnknownErrorException(getClass().getName() + "\ncheckResponseCode \n" + code);
            }
        }

        abstract SingleObservable<Connection.Answer> create();
        abstract DATA work(String data);
        abstract void onError(ErrorsContract.UnauthorizedException e);
    }
    private final SingleObservable<SyncData> syncDataProxyObservable = new NetworkObservable<SyncData>()
    {
        @Override
        SingleObservable<Connection.Answer> create()
        {
            return connection.get(API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken()));
        }
        @Override
        SyncData work(String data)
        {
            return jsonConverter.parseSyncData(data);
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            refreshTokenObservable.flat(syncDataMap).subscribe(new SingleObserver.Just<SyncData>()
            {
                @Override
                public void success(SyncData syncData)
                {
                }
            });
        }
    };
    private final Func<UserPrivateData, SingleObservable<SyncData>> syncDataMap = new Func<UserPrivateData, SingleObservable<SyncData>>()
    {
        @Override
        public SingleObservable<SyncData> call(UserPrivateData data)
        {
            settings.login(data);
            return syncDataObservable;
        }
    };
    private final SingleObservable<SyncData> syncDataObservable = new NetworkObservable<SyncData>()
    {
        @Override
        SingleObservable<Connection.Answer> create()
        {
            return connection.get(API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken()));
        }
        @Override
        SyncData work(String data)
        {
            return jsonConverter.parseSyncData(data);
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
    };
    private final SingleObservable<SyncData> syncAllObservable = new NetworkObservable<SyncData>()
    {
        @Override
        SingleObservable<Connection.Answer> create()
        {
            return connection.put(API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId())
                    ,API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken())
                    ,jsonConverter.convertSyncBody(new SynchronizationData(System.currentTimeMillis(), settings.getSyncData().getHash()))
            );
        }
        @Override
        SyncData work(String data)
        {
            return jsonConverter.parseSyncData(data);
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
    };

    private final SingleObservable<ListModel<TransactionModel>> transactionsGetObservable = new NetworkObservable<ListModel<TransactionModel>>()
    {
        @Override
        SingleObservable<Connection.Answer> create()
        {
            return connection.get(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken()));
        }
        @Override
        ListModel<TransactionModel> work(String data)
        {
            List<TransactionModel> listTransactions = jsonConverter.parseTransactions(data);
            transactions.clear();
            transactions.add(listTransactions);
            settings.setSyncData(new SynchronizationData(settings.getSyncData().getLastSyncTime(), getHash(data)));
            syncAllObservable.subscribe(new SingleObserver.Just<SyncData>()
            {
                @Override
                public void success(SyncData syncData)
                {
                    settings.setSyncData(syncData);
                }
            });
            return transactions.getAll();
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
    };
    private final SingleObservable<ListModel<TransactionModel>> transactionsPutObservable = new NetworkObservable<ListModel<TransactionModel>>()
    {
        @Override
        SingleObservable<Connection.Answer> create()
        {
            return connection.put(
                    API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId())
                    ,API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken())
                    ,jsonConverter.convertTransactions(transactions.getAll())
            );
        }
        @Override
        ListModel<TransactionModel> work(String data)
        {
            settings.setSyncData(new SynchronizationData(settings.getSyncData().getLastSyncTime(), getHash(data)));
            syncAllObservable.subscribe(new SingleObserver.Just<SyncData>()
            {
                @Override
                public void success(SyncData syncData)
                {
                    settings.setSyncData(syncData);
                }
            });
            return transactions.getAll();
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
    };
    private final Func<SyncData, SingleObservable<ListModel<TransactionModel>>> updateAllMap = new Func<SyncData, SingleObservable<ListModel<TransactionModel>>>()
    {
        @Override
        public SingleObservable<ListModel<TransactionModel>> call(SyncData data)
        {
            if(data.getLastSyncTime() > settings.getSyncData().getLastSyncTime())
            {
                return transactionsGetObservable;
            }
            else if(data.getLastSyncTime() == settings.getSyncData().getLastSyncTime() && data.getHash().equals(settings.getSyncData().getHash()))
            {
                return transactionsCacheObservable;
            }
            else
            {
                return transactionsPutObservable;
            }
        }
    };
    private final SingleObservable<ListModel<TransactionModel>> updateAllObservable = syncDataProxyObservable.flat(updateAllMap);

    private final SingleObservable<UserPrivateData> refreshTokenObservable = new NetworkObservable<UserPrivateData>()
    {
        @Override
        SingleObservable<Connection.Answer> create()
        {
            return connection.post(API.Auth.REFRESH_TOKEN, API.Auth.Params.getAuthParams(), jsonConverter.convertRefreshTokenBody(settings.getUserPrivateData().getRefreshToken()));
        }
        @Override
        UserPrivateData work(String data)
        {
            return jsonConverter.parseUserPrivateData(data);
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
    };

    private final NotifyObservable sendUpdatingsObservable = transactionsPutObservable.mapNotify();

    MainModel(Models.Transactions trnsctns, Connection cnnctn, Settings ss, JsonConverter jc)
    {
        transactions = trnsctns;
        connection = cnnctn;
        settings = ss;
        jsonConverter = jc;
    }

    @Override
    public SingleObservable<ListModel<TransactionModel>> getAll()
    {
        return transactionsCacheObservable;
    }
    @Override
    public SingleObservable<ListModel<TransactionModel>> updateAll()
    {
        return updateAllObservable;
    }
    @Override
    public NotifyObservable sendUpdatings()
    {
        return sendUpdatingsObservable;
    }

    @Override
    public SingleObservable<Integer> getBalance()
    {
        return balanceObservable;
    }

    @Override
    public void add(TransactionModel transaction)
    {
        transactions.add(transaction);
        settings.setSyncData(new SynchronizationData(settings.getSyncData().getLastSyncTime(), ""));
    }

    @Override
    public void delete(int id)
    {
        transactions.remove(id);
        settings.setSyncData(new SynchronizationData(settings.getSyncData().getLastSyncTime(), ""));
    }

    private String getHash(String data)
    {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte anArray : array)
            {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
        catch(java.security.NoSuchAlgorithmException e)
        {
        }
        return null;
    }
}