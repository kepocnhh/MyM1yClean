package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserInfo;

public class UserInfoData
    implements UserInfo
{
    static public UserInfo create(String name, GenderType gender, String avatar, long birthDate)
    {
        return new UserInfoData(name, gender, avatar, birthDate);
    }

    private final String name;
    private final GenderType gender;
    private final String avatar;
    private final long birthDate;

    private UserInfoData(String n, GenderType g, String a, long b)
    {
        name = n;
        gender = g;
        avatar = a;
        birthDate = b;
    }

    public String name()
    {
        return name;
    }
    public GenderType gender()
    {
        return gender;
    }
    public String avatar()
    {
        return avatar;
    }
    public long birthDate()
    {
        return birthDate;
    }
}