package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserPrivateData;

public class UserData
    implements UserPrivateData
{
    private String userId;
    private String userToken;
    private String refreshToken;

    public UserData(String id, String token, String rt)
    {
        userId = id;
        userToken = token;
        refreshToken = rt;
    }

    public String getUserId()
    {
        return userId;
    }
    public String getUserToken()
    {
        return userToken;
    }
    public String getRefreshToken()
    {
        return refreshToken;
    }
}