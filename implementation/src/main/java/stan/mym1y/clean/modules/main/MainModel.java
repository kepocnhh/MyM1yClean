package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.modules.users.UserData;
import stan.reactive.Func;
import stan.reactive.JustObserver;
import stan.reactive.Observable;
import stan.reactive.Observer;

class MainModel
    implements MainContract.Model
{
    private final Models.Transactions transactions;
    private final Connection connection;
    private final Settings settings;

    private final Observable<ListModel<TransactionModel>> transactionsCacheObservable = new Observable<ListModel<TransactionModel>>()
    {
        @Override
        public void subscribe(Observer<ListModel<TransactionModel>> o)
        {
            try
            {
                o.next(transactions.getAll());
                o.complete();
            }
            catch(Exception e)
            {
                o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\ncache"));
            }
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
    private final Observable<Integer> balanceObservable = transactionsCacheObservable.map(balanceMap);
    private abstract class NetworkObservable<DATA>
        extends Observable<DATA>
    {
        Observer<DATA> observer;

        @Override
        public void subscribe(Observer<DATA> o)
        {
            observer = o;
            createNetworkObservable().subscribe(new NetworkObserver<DATA>(o, getLink())
            {
                @Override
                public void next(Connection.Answer response)
                {
                    try
                    {
                        checkResponseCode(response.getCode());
                        observer.next(work(response.getData()));
                        observer.complete();
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
                        observer.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nnext " + response));
                    }
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

        abstract Observable<Connection.Answer> createNetworkObservable();
        abstract DATA work(String data);
        abstract void onError(ErrorsContract.UnauthorizedException e);
        abstract String getLink();
    }
    private abstract class NetworkObserver<DATA>
        implements Observer<Connection.Answer>
    {
        private Observer<DATA> observer;
        private String link;

        NetworkObserver(Observer<DATA> o, String l)
        {
            observer = o;
            link = l;
        }

        @Override
        public void error(Throwable t)
        {
            observer.error(new ErrorsContract.NetworkErrorException(link));
        }
        @Override
        public void complete()
        {
        }
    }
    private final Observable<SyncData> syncDataProxyObservable = new NetworkObservable<SyncData>()
    {
        @Override
        Observable<Connection.Answer> createNetworkObservable()
        {
            return connection.get(API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken()));
        }
        @Override
        SyncData work(String data)
        {
            Map responseMap = (Map)JSONParser.read(data);
            return new SynchronizationData((Long)responseMap.get("lastSyncTime"), (String)responseMap.get("hash"));
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            refreshTokenObservable.flatMap(syncDataMap);
        }
        @Override
        String getLink()
        {
            return API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId());
        }
    };
    private final Func<UserPrivateData, Observable<SyncData>> syncDataMap = new Func<UserPrivateData, Observable<SyncData>>()
    {
        @Override
        public Observable<SyncData> call(UserPrivateData data)
        {
            settings.login(data);
            return syncDataObservable;
        }
    };
    private final Observable<SyncData> syncDataObservable = new NetworkObservable<SyncData>()
    {
        @Override
        Observable<Connection.Answer> createNetworkObservable()
        {
            return connection.get(API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken()));
        }
        @Override
        SyncData work(String data)
        {
            Map responseMap = (Map)JSONParser.read(data);
            return new SynchronizationData((Long)responseMap.get("lastSyncTime"), (String)responseMap.get("hash"));
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
        @Override
        String getLink()
        {
            return API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId());
        }
    };
    private final Observable<SyncData> syncAllObservable = new NetworkObservable<SyncData>()
    {
        @Override
        Observable<Connection.Answer> createNetworkObservable()
        {
            return connection.put(API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId())
                    ,API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken())
                    ,JSONWriter.write(API.Transactions.getSyncBody(new SynchronizationData(System.currentTimeMillis(), settings.getSyncData().getHash())))
            );
        }
        @Override
        SyncData work(String data)
        {
            Map responseMap = (Map)JSONParser.read(data);
            return new SynchronizationData((Long)responseMap.get("lastSyncTime"), (String)responseMap.get("hash"));
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
        @Override
        String getLink()
        {
            return API.Transactions.getSyncLink(settings.getUserPrivateData().getUserId());
        }
    };

    private final Observable<ListModel<TransactionModel>> transactionsGetObservable = new NetworkObservable<ListModel<TransactionModel>>()
    {
        @Override
        Observable<Connection.Answer> createNetworkObservable()
        {
            return connection.get(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken()));
        }
        @Override
        ListModel<TransactionModel> work(String data)
        {
            List<TransactionModel> listTransactions = getTransactions((List)JSONParser.read(data));
            transactions.clear();
            transactions.add(listTransactions);
            settings.setSyncData(new SynchronizationData(settings.getSyncData().getLastSyncTime(), getHash(data)));
            syncAllObservable.subscribe(new JustObserver<SyncData>()
            {
                @Override
                public void next(SyncData syncData)
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
        @Override
        String getLink()
        {
            return API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId());
        }
        private List<TransactionModel> getTransactions(List list)
        {
            List<TransactionModel> listTransactions = new ArrayList<>(list.size());
            for(Object object : list)
            {
                Map map = (Map)object;
                listTransactions.add(new Transaction(
                        ((Long)map.get("id")).intValue()
                        ,((Long)map.get("count")).intValue()
                        ,(Long)map.get("date")
                ));
            }
            return listTransactions;
        }
    };
    private final Observable<ListModel<TransactionModel>> transactionsPutObservable = new NetworkObservable<ListModel<TransactionModel>>()
    {
        @Override
        Observable<Connection.Answer> createNetworkObservable()
        {
            return connection.put(
                    API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId())
                    ,API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken())
                    ,convertTransactions(transactions.getAll())
            );
        }
        @Override
        ListModel<TransactionModel> work(String data)
        {
            settings.setSyncData(new SynchronizationData(settings.getSyncData().getLastSyncTime(), getHash(data)));
            syncAllObservable.subscribe(new JustObserver<SyncData>()
            {
                @Override
                public void next(SyncData syncData)
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
        @Override
        String getLink()
        {
            return API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId());
        }
        private String convertTransactions(ListModel<TransactionModel> listModel)
        {
            List list = new ArrayList(listModel.size());
            for(int i=0; i<listModel.size(); i++)
            {
                Map map = new HashMap();
                map.put("count", listModel.get(i).getCount());
                map.put("id", listModel.get(i).getId());
                map.put("date", listModel.get(i).getDate());
                list.add(map);
            }
            return JSONWriter.write(list);
        }
    };
    private final Func<SyncData, Observable<ListModel<TransactionModel>>> updateAllMap = new Func<SyncData, Observable<ListModel<TransactionModel>>>()
    {
        @Override
        public Observable<ListModel<TransactionModel>> call(SyncData data)
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
    private final Observable<ListModel<TransactionModel>> updateAllObservable = syncDataProxyObservable.flatMap(updateAllMap);

    private final Observable<UserPrivateData> refreshTokenObservable = new NetworkObservable<UserPrivateData>()
    {
        @Override
        Observable<Connection.Answer> createNetworkObservable()
        {
            return connection.post(API.Auth.REFRESH_TOKEN, API.Auth.Params.getAuthParams(), JSONWriter.write(API.Auth.Params.getRefreshTokenBody(settings.getUserPrivateData().getRefreshToken())));
        }
        @Override
        UserPrivateData work(String data)
        {
            return getUserPrivateData(data);
        }
        @Override
        void onError(ErrorsContract.UnauthorizedException e)
        {
            observer.error(e);
        }
        @Override
        String getLink()
        {
            return API.Auth.REFRESH_TOKEN;
        }
        private UserPrivateData getUserPrivateData(String data)
        {
            Map responseBody = (Map)JSONParser.read(data);
            return new UserData((String)responseBody.get("user_id"), (String)responseBody.get("access_token"), (String)responseBody.get("refresh_token"));
        }
    };

    MainModel(Models.Transactions trnsctns, Connection cnnctn, Settings ss)
    {
        transactions = trnsctns;
        connection = cnnctn;
        settings = ss;
    }

    @Override
    public Observable<ListModel<TransactionModel>> getAll()
    {
        return transactionsCacheObservable;
    }
    @Override
    public Observable<ListModel<TransactionModel>> updateAll()
    {
        return updateAllObservable;
    }
    @Override
    public Observable<ListModel<TransactionModel>> sendUpdatings()
    {
        return transactionsPutObservable;
    }

    @Override
    public Observable<Integer> getBalance()
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