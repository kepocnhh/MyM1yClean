package stan.mym1y.clean.contracts.work;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserInfo;

public interface WorkContract
{
    interface Model
    {
        void checkUserInfo() throws UserInfoNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException;
        void setUserInfo(UserInfo info);
    }
    interface View
    {
        void error(ErrorsContract.UnauthorizedException e);
        void error();
    }
    interface Router
    {
        void toMain();
        void toUserInfo();
    }
    interface Presenter
    {
        void start();
        void setUserInfo(UserInfo info);
    }

    interface Behaviour
    {
        void logout();
    }

    class UserInfoNotExistException
        extends Exception
    {
    }
}