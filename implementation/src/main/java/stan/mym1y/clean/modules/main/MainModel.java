package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.modules.users.UserData;
import stan.reactive.Func;
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
                o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\ncache "));
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
    private final Observable<ListModel<TransactionModel>> transactionsUpdateObservable = new Observable<ListModel<TransactionModel>>()
    {
        @Override
        public void subscribe(final Observer<ListModel<TransactionModel>> o)
        {
            connection.get(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId()), API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken())
            ).subscribe(new Observer<Connection.Answer>()
            {
                @Override
                public void next(Connection.Answer response)
                {
                    try
                    {
                        checkResponseCode(response.getCode());
                        List<TransactionModel> listTransactions = getTransactions(response.getData());
                        transactions.clear();
                        transactions.add(listTransactions);
                        o.next(transactions.getAll());
                        o.complete();
                    }
                    catch(ErrorsContract.UnauthorizedException e)
                    {
                        refreshTokenObservable.subscribe(new Observer<UserPrivateData>()
                        {
                            @Override
                            public void next(UserPrivateData data)
                            {
                                settings.login(data);
                                connection.get(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId())
                                        ,API.Transactions.getTransactionsParams(settings.getUserPrivateData().getUserToken())
                                ).subscribe(new Observer<Connection.Answer>()
                                {
                                    @Override
                                    public void next(Connection.Answer answer)
                                    {
                                        try
                                        {
                                            checkResponseCode(answer.getCode());
                                            List<TransactionModel> listTransactions = getTransactions(answer.getData());
                                            transactions.clear();
                                            transactions.add(listTransactions);
                                            o.next(transactions.getAll());
                                            o.complete();
                                        }
                                        catch(ErrorsContract.UnauthorizedException | ErrorsContract.UnknownErrorException e)
                                        {
                                            o.error(e);
                                        }
                                        catch(Exception e)
                                        {
                                            o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nnext " + answer));
                                        }
                                    }
                                    @Override
                                    public void error(Throwable t)
                                    {
                                        o.error(new ErrorsContract.NetworkErrorException(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId())));
                                    }
                                    @Override
                                    public void complete()
                                    {
                                    }
                                });
                            }
                            @Override
                            public void error(Throwable t)
                            {
                                o.error(t);
                            }
                            @Override
                            public void complete()
                            {
                            }
                        });
                    }
                    catch(ErrorsContract.UnknownErrorException e)
                    {
                        o.error(e);
                    }
                    catch(Exception e)
                    {
                        o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nnext " + response));
                    }
                }
                @Override
                public void error(Throwable t)
                {
                    o.error(new ErrorsContract.NetworkErrorException(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId())));
                }
                @Override
                public void complete()
                {
                }
            });
        }
        private void checkResponseCode(int code)
                throws ErrorsContract.UnauthorizedException, ErrorsContract.UnknownErrorException
        {
            if(code == 401)
            {
                throw new ErrorsContract.UnauthorizedException("code: " + code);
            }
            else if(code != 200)
            {
                throw new ErrorsContract.UnknownErrorException(getClass().getName() + "\ncheckResponseCode \n" + code);
            }
        }
        private List<TransactionModel> getTransactions(String data)
        {
            List list = (List)JSONParser.read(data);
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
    private final Observable<UserPrivateData> refreshTokenObservable = new Observable<UserPrivateData>()
    {
        @Override
        public void subscribe(final Observer<UserPrivateData> o)
        {
            connection.post(API.Auth.REFRESH_TOKEN, API.Auth.Params.getAuthParams()
                    ,JSONWriter.write(API.Auth.Params.getRefreshTokenBody(settings.getUserPrivateData().getRefreshToken()))
            ).subscribe(new Observer<Connection.Answer>()
            {
                @Override
                public void next(Connection.Answer response)
                {
                    try
                    {
                        checkResponseCode(response.getCode());
                        o.next(getUserPrivateData(response.getData()));
                        o.complete();
                    }
                    catch(ErrorsContract.InvalidDataException | ErrorsContract.UnknownErrorException e)
                    {
                        o.error(e);
                    }
                    catch(Exception e)
                    {
                        o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nnext " + response));
                    }
                }
                @Override
                public void error(Throwable t)
                {
                    o.error(new ErrorsContract.NetworkErrorException(API.Transactions.getTransactionsLink(settings.getUserPrivateData().getUserId())));
                }
                @Override
                public void complete()
                {
                }
            });
        }
        private void checkResponseCode(int code)
                throws ErrorsContract.InvalidDataException, ErrorsContract.UnknownErrorException
        {
            if(code == 400)
            {
                throw new ErrorsContract.InvalidDataException("answer: " + code);
            }
            else if(code != 200)
            {
                throw new ErrorsContract.UnknownErrorException("answer: " + code);
            }
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
        return transactionsUpdateObservable;
    }

    @Override
    public Observable<Integer> getBalance()
    {
        return balanceObservable;
    }

    @Override
    public void add(TransactionModel transaction)
    {
    }

    @Override
    public void delete(int id)
    {
    }
}