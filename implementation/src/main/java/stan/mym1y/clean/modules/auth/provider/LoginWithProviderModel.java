package stan.mym1y.clean.modules.auth.provider;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginWithProviderContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserProviderData;
import stan.mym1y.clean.data.remote.apis.AuthApi;

class LoginWithProviderModel
    implements LoginWithProviderContract.Model
{
    private final AuthApi authApi;

    LoginWithProviderModel(AuthApi authApi)
    {
        this.authApi = authApi;
    }

    public UserProviderData getToken(String code)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        return authApi.postUserProviderData(code);
    }
    public UserPrivateData login(UserProviderData data, Providers.Type type)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        return authApi.postLogin(data, type);
    }
}