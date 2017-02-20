package stan.mym1y.clean.contracts.auth;

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
        void login(String token);
    }

    class NetworkErrorException
            extends Exception
    {
    }
    class UnauthorizedException
            extends Exception
    {
    }
    class InvalidDataException
            extends Exception
    {
    }
    class ServerErrorException
            extends Exception
    {
    }
    class UnknownErrorException
            extends Exception
    {
    }
}