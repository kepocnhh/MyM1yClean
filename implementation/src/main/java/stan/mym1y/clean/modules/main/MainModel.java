package stan.mym1y.clean.modules.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import stan.json.JSONParser;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.reactive.Observable;
import stan.reactive.Observer;

class MainModel
    implements MainContract.Model
{
    private final Models.Transactions transactions;
    private final Connection connection;
    private final UserPrivateData userPrivateData;

    private final Observable<ListModel<TransactionModel>> transactionsObservable = new Observable<ListModel<TransactionModel>>()
    {
        @Override
        public void subscribe(final Observer<ListModel<TransactionModel>> o)
        {
            connection.get(API.Transactions.getTransactionsLink(userPrivateData.getUserId()), API.Transactions.getTransactionsParams(userPrivateData.getUserToken())).subscribe(new Observer<Connection.Answer>()
            {
                @Override
                public void next(Connection.Answer response)
                {
                    getAll(o, response);
                }
                @Override
                public void error(Throwable t)
                {
                    o.error(new ErrorsContract.NetworkErrorException(API.Transactions.getTransactionsLink(userPrivateData.getUserId())));
                }
                @Override
                public void complete()
                {
                }
            });
        }
    };
    private void getAll(Observer<ListModel<TransactionModel>> o, Connection.Answer response)
    {
        if(response.getCode() == 200)
        {
            getAll(o, (List)JSONParser.read(response.getData()));
            o.complete();
        }
        else if(response.getCode() == 400)
        {
            o.error(new ErrorsContract.UnauthorizedException("login\n" + response.getData() + "\n" + response.getCode()));
        }
        else
        {
            o.error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nlogin\n" + response.getData()));
        }
    }
    private void getAll(Observer<ListModel<TransactionModel>> o, List list)
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
        transactions.clear();
        transactions.add(listTransactions);
        o.next(transactions.getAll());
    }

    MainModel(Models.Transactions trnsctns, Connection cnnctn, UserPrivateData data)
    {
        transactions = trnsctns;
        connection = cnnctn;
        userPrivateData = data;
    }

    @Override
    public Observable<ListModel<TransactionModel>> getAll()
    {
        return transactionsObservable;
    }

    @Override
    public Observable<Integer> getBalance()
    {
        return null;
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