package stan.mym1y.clean.modules.work;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.WorkContract;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;
import stan.mym1y.clean.modules.data.InitData;

class WorkModel
    implements WorkContract.Model
{
    private final Settings settings;
    private final AuthApi authApi;
    private final PrivateDataApi privateDataApi;

    WorkModel(Settings sttngs, AuthApi aa, PrivateDataApi pda)
    {
        settings = sttngs;
        authApi = aa;
        privateDataApi = pda;
    }

    public void checkUserInfo()
            throws WorkContract.UserInfoNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        try
        {
            checkUserInfoLast();
        }
        catch(ErrorsContract.UnauthorizedException e)
        {
            try
            {
                settings.login(authApi.postRefreshToken(settings.getUserPrivateData().refreshToken()));
            }
            catch(ErrorsContract.NetworkException networkException)
            {
                throw new ErrorsContract.UnknownException(networkException);
            }
            checkUserInfoLast();
        }
    }
    private void checkUserInfoLast()
            throws WorkContract.UserInfoNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        try
        {
            settings.setUserInfo(InitData.create(true, privateDataApi.getUserInfo(settings.getUserPrivateData())));
        }
        catch(ErrorsContract.DataNotExistException e)
        {
            throw new WorkContract.UserInfoNotExistException();
        }
        catch(ErrorsContract.NetworkException e)
        {
            if(!settings.getUserInfo().init())
            {
                throw new ErrorsContract.UnknownException(e);
            }
        }
        catch(ErrorsContract.UnknownException e)
        {
            if(!settings.getUserInfo().init())
            {
                throw e;
            }
        }
    }
    public void setUserInfo(UserInfo info)
    {
        settings.setUserInfo(InitData.create(true, info));
    }
}