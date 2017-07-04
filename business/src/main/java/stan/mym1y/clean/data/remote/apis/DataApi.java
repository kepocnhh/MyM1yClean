package stan.mym1y.clean.data.remote.apis;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public interface DataApi
{
    String BASE_URL = "https://mym1yclean.firebaseio.com/";

    interface Get
    {
        String VERSIONS = BASE_URL + "versions.json";
        String CURRENCIES = BASE_URL + "currency.json";
        String CASH_ACCOUNTS = BASE_URL + "cashaccounts";
        String SYNC = BASE_URL + "sync";
    }

    interface Codes
    {
        int UNAUTHORIZED = 401;
        int SUCCESS = 200;
    }

    SingleObservable<Versions> getVersions();
    SingleObservable<List<Currency>> getCurrencies();
    SingleObservable<List<CashAccountRequest>> getTransactions(UserPrivateData data);
    SingleObservable<SyncData> getSyncData(UserPrivateData data);
    NotifyObservable putTransactions(UserPrivateData data, CashAccountRequest cashAccountRequest);
    NotifyObservable putTransactions(UserPrivateData data, List<CashAccountRequest> cashAccountRequests);
    NotifyObservable putSyncData(UserPrivateData data, SyncData syncData);
}