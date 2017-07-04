package stan.mym1y.clean.components;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;

public interface Settings
{
    Versions getVersions();
    void setVersions(Versions versions);

    UserPrivateData getUserPrivateData();
    void login(UserPrivateData data);
    void logout();

    SyncData getSyncData();
    void setSyncData(SyncData data);
}