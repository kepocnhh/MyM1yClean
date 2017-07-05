package stan.mym1y.clean.data.remote.apis;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.remote.Connection;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public interface PrivateDataApi
{
    String BASE_URL = Connection.BASE_DATA_URL + "/private/";

    class Get
    {
        static public String getCashAccountsUrl(UserPrivateData data)
        {
            return BASE_URL + data.userId() + "/cashaccounts.json";
        }
        static public String getCashAccountUrl(UserPrivateData data, CashAccount cashAccount)
        {
            return BASE_URL + data.userId() + "/cashaccounts/" + cashAccount.uuid() + "/.json";
        }
        static public String getSyncUrl(UserPrivateData data)
        {
            return BASE_URL + data.userId() + "/sync.json";
        }
    }

    interface Codes
    {
        int UNAUTHORIZED = 401;
        int SUCCESS = 200;
    }

    SingleObservable<List<CashAccountRequest>> getTransactions(UserPrivateData data);
    SingleObservable<SyncData> getSyncData(UserPrivateData data);
    NotifyObservable putTransactions(UserPrivateData data, CashAccountRequest cashAccountRequest);
    NotifyObservable putTransactions(UserPrivateData data, List<CashAccountRequest> cashAccountRequests);
    NotifyObservable putSyncData(UserPrivateData data, SyncData syncData);
}