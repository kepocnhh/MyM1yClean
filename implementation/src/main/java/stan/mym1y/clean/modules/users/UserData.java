package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserPrivateData;

public class UserData
    implements UserPrivateData
{
    private String userId;
    private String userToken;

    public UserData(String id, String token)
    {
        userId = id;
        userToken = token;
    }

    @Override
    public String getUserId()
    {
        return userId;
    }
    @Override
    public String getUserToken()
    {
        return userToken;
    }
}