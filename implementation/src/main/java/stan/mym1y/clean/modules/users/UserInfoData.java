package stan.mym1y.clean.modules.users;

import stan.mym1y.clean.cores.users.UserInfo;

public class UserInfoData
    implements UserInfo
{
    private final String name;
    private final GenderType gender;
    private final String avatar;
    private final long birthDate;

    public UserInfoData(String name, GenderType gender, String avatar, long birthDate)
    {
        this.name = name;
        this.gender = gender;
        this.avatar = avatar;
        this.birthDate = birthDate;
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