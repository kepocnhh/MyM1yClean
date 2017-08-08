package stan.mym1y.clean.components;

import java.util.List;

import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserProviderData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.cores.versions.Versions;

public interface JsonConverter
{
    String get(UserSecretData data);
    String get(UserProviderData data, Providers.Type type);
    String getRefreshTokenBody(String refreshToken);
    String get(List<Transaction> transactions);
    String get(SyncData syncData);
    String get(CashAccountRequest cashAccountRequest);
    String getCashAccountRequests(List<CashAccountRequest> cashAccountRequests);

    Versions getVersions(String json) throws ParseException;
    List<Currency> getCurrencies(String json) throws ParseException;
    UserPrivateData getUserPrivateData(String string) throws ParseException;
    UserPrivateData getUserPrivateDataAfterRefresh(String string) throws ParseException;
    UserProviderData getUserProviderData(String json) throws ParseException;
    SyncData getSyncData(String string) throws ParseException;
    List<CashAccountRequest> getCashAccounts(String json) throws ParseException;

    class ParseException
            extends Exception
    {
        public ParseException(Throwable t)
        {
            super(t);
        }
    }
}