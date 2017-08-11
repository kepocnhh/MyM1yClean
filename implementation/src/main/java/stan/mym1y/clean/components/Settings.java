package stan.mym1y.clean.components;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;

public interface Settings
{
    Versions getVersions();
    void setVersions(Versions versions);

    UserPrivateData getUserPrivateData();
    void login(UserPrivateData data);
    void logout();

    UserInfo getUserInfo();
    void setUserInfo(UserInfo info);

    SyncData getSyncData();
    void setSyncData(SyncData data);

    Theme getCurrentTheme();
    void setDarkTheme();
    void setLightTheme();
}