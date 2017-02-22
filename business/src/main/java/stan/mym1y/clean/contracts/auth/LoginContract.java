package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.reactive.Observable;

public interface LoginContract
{
    interface Model
    {
        void checkData(String login, String password) throws ValidateDataException;
        Observable<UserPrivateData> login(String login, String password);
    }
    interface View
    {
        void error(ErrorsContract.NetworkErrorException exception);
        void error(ErrorsContract.UnauthorizedException exception);
        void error(ErrorsContract.InvalidDataException exception);
        void error(ErrorsContract.ServerErrorException exception);
        void error(ErrorsContract.UnknownErrorException exception);
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