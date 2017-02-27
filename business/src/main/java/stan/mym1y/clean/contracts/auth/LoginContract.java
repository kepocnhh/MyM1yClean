package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.reactive.single.SingleObservable;

public interface LoginContract
{
    interface Model
    {
        void checkData(String login, String password) throws ValidateDataException;
        SingleObservable<UserPrivateData> login(String login, String password);
    }
    interface View
    {
        void error(ErrorsContract.NetworkErrorException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error(ErrorsContract.InvalidDataException e);
        void error(ErrorsContract.ServerErrorException e);
        void error(ErrorsContract.UnknownErrorException e);
        void error(ValidateDataException exception);
        void success(UserPrivateData data);
    }
    interface Presenter
    {
        void login(String login, String password);
    }

    interface Behaviour
    {
        void login(UserPrivateData data);
        void toSignup();
    }

    class ValidateDataException
            extends Exception
    {
    }
}