package stan.mym1y.clean.data.remote.apis;

import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.reactive.single.SingleObservable;

public interface AuthApi
{
    String SERVER_KEY = "AIzaSyBaxJYzOa-DtvZ9QRVQn6WkHEc2-tTKsEQ";
    String BASE_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/";

    interface Post
    {
        String LOGIN = BASE_URL + "verifyPassword";
        String REGISTRATION = BASE_URL + "signupNewUser";
        String REFRESH_TOKEN = "https://securetoken.googleapis.com/v1/token";
    }
    interface Codes
    {
        int UNAUTHORIZED = 400;
        int SUCCESS = 200;
    }

    SingleObservable<UserPrivateData> postLogin(UserSecretData data);
    SingleObservable<UserPrivateData> postRegistration(UserSecretData data);
    SingleObservable<UserPrivateData> postRefreshToken(String refreshToken);
}