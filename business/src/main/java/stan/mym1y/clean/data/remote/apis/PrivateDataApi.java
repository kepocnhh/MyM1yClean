package stan.mym1y.clean.data.remote.apis;

import java.util.List;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.remote.Connection;

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
        static public String getUserInfoUrl(UserPrivateData data)
        {
            return BASE_URL + data.userId() + "/user.json";
        }
    }

    interface Codes
    {
        int UNAUTHORIZED = 401;
        int SUCCESS = 200;
    }

    List<CashAccountRequest> getTransactions(UserPrivateData data) throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    SyncData getSyncData(UserPrivateData data) throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    void putTransactions(UserPrivateData data, CashAccountRequest cashAccountRequest) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    void putTransactions(UserPrivateData data, List<CashAccountRequest> cashAccountRequests) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    void putSyncData(UserPrivateData data, SyncData syncData) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    UserInfo getUserInfo(UserPrivateData data) throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
}