package stan.mym1y.clean.contracts.auth;

public interface RegistrationContract
{
    interface Model
    {

    }
    interface View
    {
        void error(AuthContract.NetworkErrorException exception);
        void error(AuthContract.InvalidDataException exception);
        void error(AuthContract.ServerErrorException exception);
        void error(AuthContract.UnknownErrorException exception);
    }
    interface Presenter
    {
        void registration(String login, String password);
    }

    interface Behaviour
    {
        void registration(String token);
    }
}