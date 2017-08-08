package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;

public interface AuthContract
{
    interface View
    {
    }
    interface Router
    {
        void toLogin();
        void toLogin(Providers.Type type);
        void toRegistration();
    }
    interface Presenter
    {
        void toLogin();
        void toLogin(Providers.Type type);
        void toRegistration();
    }

    enum Screen
    {
        LOGIN,
        REGISTRATION,
        LOGIN_PROVIDER,
    }

    interface Behaviour
    {
        void enter(UserPrivateData data);
    }
}