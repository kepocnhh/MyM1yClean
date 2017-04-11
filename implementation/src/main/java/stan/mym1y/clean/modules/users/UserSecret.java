package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserSecretData;

public class UserSecret
    implements UserSecretData
{
    private final String login;
    private final String password;

    public UserSecret(String l, String p)
    {
        login = l;
        password = p;
    }

    public String getLogin()
    {
        return login;
    }
    public String getPassword()
    {
        return password;
    }
}