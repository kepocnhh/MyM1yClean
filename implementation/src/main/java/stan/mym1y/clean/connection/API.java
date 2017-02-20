package stan.mym1y.clean.connection;

public interface API
{
    String SERVER_KEY = "AIzaSyBaxJYzOa-DtvZ9QRVQn6WkHEc2-tTKsEQ";

    interface Auth
    {
        String BASE_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";
        String LOGIN = BASE_URL + "verifyPassword";
        interface Params
        {
            String KEY = "key";
        }
    }
}