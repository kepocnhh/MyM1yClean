package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserProviderData;

public class UserProvider
    implements UserProviderData
{
    private final String tokenId;

    public UserProvider(String tokenId)
    {
        this.tokenId = tokenId;
    }

    public String tokenId()
    {
        return tokenId;
    }
}