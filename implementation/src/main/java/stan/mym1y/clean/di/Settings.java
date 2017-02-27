package stan.mym1y.clean.di;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;

public interface Settings
{
    UserPrivateData getUserPrivateData();
    void login(UserPrivateData userPrivateData);
    void logout();

    SyncData getSyncData();
    void setSyncData(SyncData data);

}