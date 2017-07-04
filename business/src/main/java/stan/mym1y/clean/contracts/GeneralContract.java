package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.users.UserPrivateData;

public interface GeneralContract
{
    interface Model
    {
        UserPrivateData getUserPrivateData() throws UserNotAuthorizedException;
        void login(UserPrivateData data);
        void logout();
        void clearAll();
    }
    interface View
    {
    }
    interface Router
    {
        void toStart();
        void toAuth();
        void toMain();
    }
    interface Presenter
    {
        void start();
        void checkAuth();
        void enter(UserPrivateData data);
        void logout();
    }

    class UserNotAuthorizedException
        extends Exception
    {
    }
}