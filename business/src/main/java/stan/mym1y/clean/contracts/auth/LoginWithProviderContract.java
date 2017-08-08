package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserProviderData;

public interface LoginWithProviderContract
{
    interface Model
    {
        UserProviderData getToken(String code) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
        UserPrivateData login(UserProviderData data, Providers.Type type) throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error();
        void success(UserPrivateData data);
    }
    interface Presenter
    {
        void login(Providers.Type type, String code);
    }

    interface Behaviour
    {
        void login(UserPrivateData data);
        void exit();
    }
}