package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.cores.users.UserPrivateData;

public interface AuthContract
{
    interface View
    {
    }
    interface Router
    {
        void toLogin();
        void toRegistration();
    }
    interface Presenter
    {
        void toLogin();
        void toRegistration();
    }

    interface Behaviour
    {
        void enter(UserPrivateData data);
    }
}