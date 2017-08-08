package stan.mym1y.clean.data.remote.apis;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserProviderData;
import stan.mym1y.clean.cores.users.UserSecretData;

public interface AuthApi
{
    String SERVER_KEY = "AIzaSyBaxJYzOa-DtvZ9QRVQn6WkHEc2-tTKsEQ";
    String GOOGLEAPIS = "https://www.googleapis.com/";
    String BASE_URL = GOOGLEAPIS + "identitytoolkit/v3/relyingparty/";
    String CLIENT_ID = "951140010429-ddepbsp5kch6o3vj30g4bgkrl571p4do.apps.googleusercontent.com";
    String CLIENT_SECRET = "gw1x7iL6_4Zrn6Xdvx_dq76-";
    String REDIRECT_URI = "https://mym1y.app/auth/handler";

    interface Post
    {
        String GET_TOKEN_PROVIDER = GOOGLEAPIS + "oauth2/v4/token";
        String LOGIN_WITH_PROVIDER = BASE_URL + "verifyAssertion";
        String LOGIN = BASE_URL + "verifyPassword";
        String REGISTRATION = BASE_URL + "signupNewUser";
        String REFRESH_TOKEN = "https://securetoken.googleapis.com/v1/token";
    }
    interface Codes
    {
        int UNAUTHORIZED = 400;
        int SUCCESS = 200;
    }

    UserProviderData postUserProviderData(String code) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    UserPrivateData postLogin(UserProviderData data, Providers.Type type) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    UserPrivateData postLogin(UserSecretData data) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    UserPrivateData postRegistration(UserSecretData data) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    UserPrivateData postRefreshToken(String refreshToken) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
}