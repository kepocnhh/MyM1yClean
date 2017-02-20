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
            extends RuntimeException
    {
    }
    class UnauthorizedException
            extends RuntimeException
    {
    }
    class InvalidDataException
            extends RuntimeException
    {
    }
    class ServerErrorException
            extends RuntimeException
    {
    }
    class UnknownErrorException
            extends RuntimeException
    {
    }
}