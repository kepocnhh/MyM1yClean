package stan.mym1y.clean.contracts.work;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserInfo;

public interface UserInfoCreateContract
{
    interface Model
    {
        void checkData(UserInfo info) throws ValidateDataException;
        void save(UserInfo info)
                throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
    }
    interface View
    {
        void error(ErrorsContract.NetworkException e);
        void error(ErrorsContract.UnauthorizedException e);
        void error(ValidateDataException e);
        void error();
        void success(UserInfo info);
    }
    interface Presenter
    {
        void save(UserInfo info);
    }

    interface Behaviour
    {
        void success(UserInfo info);
        void unauthorized();
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
            EMPTY_NAME,
        }
    }
}