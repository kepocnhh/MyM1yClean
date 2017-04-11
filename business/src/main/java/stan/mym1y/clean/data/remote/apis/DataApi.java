package stan.mym1y.clean.data.remote.apis;

import java.util.List;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public interface DataApi
{
    String BASE_URL = "https://mym1yclean.firebaseio.com/";

    interface Get
    {
        String TRANSACTIONS = BASE_URL + "transactions";
        String SYNC = BASE_URL + "sync";
    }

    interface Codes
    {
        int UNAUTHORIZED = 401;
        int SUCCESS = 200;
    }

    SingleObservable<List<Transaction>> getTransactions(UserPrivateData data);
    SingleObservable<SyncData> getSyncData(UserPrivateData data);
    NotifyObservable putTransactions(UserPrivateData data, List<Transaction> transactions);
    NotifyObservable putSyncData(UserPrivateData data, SyncData syncData);
}