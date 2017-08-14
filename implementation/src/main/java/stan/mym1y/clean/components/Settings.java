package stan.mym1y.clean.components;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.Init;

public interface Settings
{
    Init<Versions> getVersions();
    void setVersions(Init<Versions> versions);

    UserPrivateData getUserPrivateData();
    void login(UserPrivateData data);
    void logout();

    Init<UserInfo> getUserInfo();
    void setUserInfo(Init<UserInfo> info);

    SyncData getSyncData();
    void setSyncData(SyncData data);

    Theme getCurrentTheme();
    void setDarkTheme();
    void setLightTheme();
}