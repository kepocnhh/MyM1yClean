package stan.mym1y.clean.connection;

import java.util.HashMap;
import java.util.Map;

public interface API
{
    String SERVER_KEY = "AIzaSyBaxJYzOa-DtvZ9QRVQn6WkHEc2-tTKsEQ";

    interface Auth
    {
        String BASE_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";
        String LOGIN = BASE_URL + "verifyPassword";
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
        }
    }

    String BASE_URL = "https://mym1yclean.firebaseio.com/";
    String TRANSACTIONS = BASE_URL + "transactions";
    class Transactions
    {
        static public String getTransactionsLink(String userId)
        {
            return TRANSACTIONS + "/" +userId+ ".json";
        }
        static public Map<String, String> getTransactionsParams(String userToken)
        {
            Map<String, String> params = new HashMap<>();
            params.put("auth", userToken);
            return params;
        }
    }
}