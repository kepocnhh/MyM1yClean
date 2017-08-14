package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.Init;

public interface GeneralContract
{
    interface Model
    {
        Versions getActualVersions()
                throws ErrorsContract.UnknownException;
        Init<Versions> getCacheVersions();
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
        void toWork();
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