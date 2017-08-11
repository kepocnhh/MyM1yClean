package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;

public interface AuthContract
{
    interface Model
    {
        void checkUserInfo(UserPrivateData data)
                throws UserInfoNotExistException, ErrorsContract.UnknownException;
    }
    interface View
    {
        void error();
        void success(UserPrivateData data);
    }
    interface Router
    {
        void toLogin();
        void toLogin(Providers.Type type);
        void toRegistration();
        void toUserInfo();
    }
    interface Presenter
    {
        void toLogin();
        void toLogin(Providers.Type type);
        void toRegistration();
        void enter(UserPrivateData data);
    }

    enum Screen
    {
        LOGIN,
        REGISTRATION,
        LOGIN_PROVIDER,
        USER_INFO,
    }

    interface Behaviour
    {
        void enter(UserPrivateData data);
    }

    class UserInfoNotExistException
            extends Exception
    {
    }
}