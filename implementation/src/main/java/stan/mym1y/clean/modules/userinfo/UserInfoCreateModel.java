package stan.mym1y.clean.modules.userinfo;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.UserInfoCreateContract;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;

class UserInfoCreateModel
    implements UserInfoCreateContract.Model
{
    private final Settings settings;
    private final PrivateDataApi privateDataApi;

    public UserInfoCreateModel(Settings ss, PrivateDataApi pda)
    {
        settings = ss;
        privateDataApi = pda;
    }

    public void checkData(UserInfo userInfo)
            throws UserInfoCreateContract.ValidateDataException
    {
        if(userInfo.name() == null || userInfo.name().isEmpty())
        {
            throw new UserInfoCreateContract.ValidateDataException(UserInfoCreateContract.ValidateDataException.Error.EMPTY_NAME);
        }
    }
    public void save(UserInfo userInfo)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        privateDataApi.putUserInfo(settings.getUserPrivateData(), userInfo);
    }
}