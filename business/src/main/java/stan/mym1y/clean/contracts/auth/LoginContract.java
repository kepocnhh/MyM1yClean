package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.reactive.single.SingleObservable;

public interface LoginContract
{
    interface Model
    {
        void checkData(String login, String password) throws ValidateDataException;
        SingleObservable<UserPrivateData> login(UserSecretData data);
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error(ValidateDataException exception);
        void error();
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
        private final Error error;

        public ValidateDataException(Error e)
        {
            error = e;
        }

        public Error error()
        {
            return error;
        }

        public enum Error
        {
            EMPTY_PASSWORD,
            EMPTY_LOGIN,
            LOGIN_VALID,
            PASSWORD_LENGTH,
        }
    }
}