package stan.mym1y.clean.di;

public interface Settings
{
    int getSortingType();
    void setSortyngType(int type);

    String getUserToken();
    void login(String token);
}