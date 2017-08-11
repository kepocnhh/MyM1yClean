package stan.mym1y.clean.modules.auth;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;

class AuthModel
    implements AuthContract.Model
{
    private final Settings settings;
    private final PrivateDataApi privateDataApi;

    AuthModel(Settings sttngs, PrivateDataApi pda)
    {
        settings = sttngs;
        privateDataApi = pda;
    }

    public void checkUserInfo(UserPrivateData data)
            throws AuthContract.UserInfoNotExistException, ErrorsContract.UnknownException
    {
        try
        {
            settings.setUserInfo(privateDataApi.getUserInfo(data));
        }
        catch(ErrorsContract.DataNotExistException e)
        {
            throw new AuthContract.UserInfoNotExistException();
        }
        catch(ErrorsContract.NetworkException | ErrorsContract.UnauthorizedException e)
        {
            throw new ErrorsContract.UnknownException(e);
        }
    }
}