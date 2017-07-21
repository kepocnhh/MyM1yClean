package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.reactive.single.SingleObservable;

public interface GeneralContract
{
    interface Model
    {
        SingleObservable<Versions> getActualVersions();
        Versions getCacheVersions();
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