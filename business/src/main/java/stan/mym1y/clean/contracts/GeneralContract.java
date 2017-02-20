package stan.mym1y.clean.contracts;

public interface GeneralContract
{
    interface Model
    {
        String getToken() throws UserNotAuthorizedException;
    }
    interface View
    {
    }
    interface Router
    {
        void toAuth();
        void toMain(String token);
    }
    interface Presenter
    {
        void checkAuth();
        void enter(String token);
    }

    class UserNotAuthorizedException
        extends Exception
    {
    }
}