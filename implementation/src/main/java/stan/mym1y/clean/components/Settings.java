package stan.mym1y.clean.components;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;

public interface Settings
{
    UserPrivateData getUserPrivateData();
    void login(UserPrivateData data);
    void logout();

    SyncData getSyncData();
    void setSyncData(SyncData data);
}