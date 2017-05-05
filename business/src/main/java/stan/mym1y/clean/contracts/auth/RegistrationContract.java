package stan.mym1y.clean.contracts.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.reactive.single.SingleObservable;

public interface RegistrationContract
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
        void error(ValidateDataException e);
        void error();
        void success(UserPrivateData data);
    }
    interface Presenter
    {
        void registration(String login, String password);
    }

    interface Behaviour
    {
        void registration(UserPrivateData data);
        void toSignin();
    }

    class ValidateDataException
            extends Exception
    {
        public final Error error;

        public ValidateDataException(Error e)
        {
            error = e;
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