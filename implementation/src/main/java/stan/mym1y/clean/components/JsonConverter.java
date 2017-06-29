package stan.mym1y.clean.components;

import java.util.List;

import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;

public interface JsonConverter
{
    String get(UserSecretData data);
    String getRefreshTokenBody(String refreshToken);
    String get(List<Transaction> transactions);
    String get(SyncData syncData);
    String get(CashAccountRequest cashAccountRequest);
    String getCashAccountRequests(List<CashAccountRequest> cashAccountRequests);

    UserPrivateData getUserPrivateData(String string) throws ParseException;
    UserPrivateData getUserPrivateDataAfterRefresh(String string) throws ParseException;
    List<Transaction> getTransactions(String string) throws ParseException;
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