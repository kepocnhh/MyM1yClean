package stan.mym1y.clean.contracts.auth;

public interface LoginContract
{
    interface Model
    {
        void checkData(String login, String password) throws ValidateDataException;
    }
    interface View
    {
        void error(AuthContract.NetworkErrorException exception);
        void error(AuthContract.UnauthorizedException exception);
        void error(AuthContract.InvalidDataException exception);
        void error(AuthContract.ServerErrorException exception);
        void error(AuthContract.UnknownErrorException exception);
        void error(ValidateDataException exception);
        void sucess(String token);
    }
    interface Presenter
    {
        void login(String login, String password);
    }

    interface Behaviour
    {
        void login(String token);
    }

    class ValidateDataException
            extends Exception
    {
    }
}