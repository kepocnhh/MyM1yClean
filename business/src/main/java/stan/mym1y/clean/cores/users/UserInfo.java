package stan.mym1y.clean.cores.users;

public interface UserInfo
{
    String name();
    GenderType gender();
    String avatar();
    long birthDate();

    enum GenderType
    {
        MALE,
        FEMALE
    }
}