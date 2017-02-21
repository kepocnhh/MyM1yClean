package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;

public interface RegistrationContract
{
    interface Model
    {

    }
    interface View
    {
        void error(ErrorsContract.NetworkErrorException exception);
        void error(ErrorsContract.InvalidDataException exception);
        void error(ErrorsContract.ServerErrorException exception);
        void error(ErrorsContract.UnknownErrorException exception);
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