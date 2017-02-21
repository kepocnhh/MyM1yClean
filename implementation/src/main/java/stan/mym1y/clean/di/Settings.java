package stan.mym1y.clean.di;

import stan.mym1y.clean.cores.users.UserPrivateData;

public interface Settings
{
    int getSortingType();
    void setSortyngType(int type);

    UserPrivateData getUserPrivateData();
    void login(UserPrivateData userPrivateData);
    void logout();
}