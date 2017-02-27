package stan.mym1y.clean.connection;

import java.util.HashMap;
import java.util.Map;

import stan.mym1y.clean.cores.sync.SyncData;

public interface API
{
    String SERVER_KEY = "AIzaSyBaxJYzOa-DtvZ9QRVQn6WkHEc2-tTKsEQ";

    interface Auth
    {
        String BASE_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";
        String LOGIN = BASE_URL + "verifyPassword";
        String REGISTRATION = BASE_URL + "signupNewUser";
        String REFRESH_TOKEN = "https://securetoken.googleapis.com/v1/token";
        class Params
        {
            static public Map<String, String> getAuthParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("key", API.SERVER_KEY);
                return params;
            }
            static public Map getAuthBody(String login, String password)
            {
                Map<String, Object> body = new HashMap<>();
                body.put("email", login);
                body.put("password", password);
                body.put("returnSecureToken", true);
                return body;
            }
            static public Map getRefreshTokenBody(String refreshToken)
            {
                Map<String, Object> body = new HashMap<>();
                body.put("grant_type", "refresh_token");
                body.put("refresh_token", refreshToken);
                return body;
            }
        }
        interface Codes
        {
            int UNAUTHORIZED = 400;
            int SUCCESS = 200;
        }
    }

    String BASE_URL = "https://mym1yclean.firebaseio.com/";
    String TRANSACTIONS = BASE_URL + "transactions";
    String SYNC = BASE_URL + "sync";
    class Transactions
    {
        static public String getTransactionsLink(String userId)
        {
            return TRANSACTIONS + "/" +userId+ ".json";
        }
        static public String getSyncLink(String userId)
        {
            return SYNC + "/" +userId+ ".json";
        }
        static public Map<String, String> getTransactionsParams(String userToken)
        {
            Map<String, String> params = new HashMap<>();
            params.put("auth", userToken);
            return params;
        }
        static public Map getSyncBody(SyncData syncData)
        {
            Map<String, Object> body = new HashMap<>();
            body.put("lastSyncTime", syncData.getLastSyncTime());
            body.put("hash", syncData.getHash());
            return body;
        }
    }
}