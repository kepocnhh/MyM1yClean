package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserPrivateData;

public class UserData
    implements UserPrivateData
{
    private final String userId;
    private final String userToken;
    private final String refreshToken;

    public UserData(String id, String token, String rt)
    {
        userId = id;
        userToken = token;
        refreshToken = rt;
    }

    public String userId()
    {
        return userId;
    }
    public String userToken()
    {
        return userToken;
    }
    public String refreshToken()
    {
        return refreshToken;
    }
}