package stan.mym1y.clean.cases;

import java.util.HashMap;
import java.util.Map;

import stan.boxes.Case;
import stan.boxes.ORM;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.Init;
import stan.mym1y.clean.modules.data.InitData;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.ui.ColorsData;
import stan.mym1y.clean.modules.ui.ThemeData;
import stan.mym1y.clean.modules.users.UserData;
import stan.mym1y.clean.modules.users.UserInfoData;
import stan.mym1y.clean.modules.versions.VersionsData;

public class Cases
    implements Settings
{
    private final Case<Init<Versions>> versionsCase;
    private final Case<UserPrivateData> userPrivateDataCase;
    private final Case<SyncData> syncDataCase;
    private final Case<Init<UserInfo>> userInfoCase;
    private final Case<Theme> themeCase;

    private final Theme darkTheme;
    private final Theme lightTheme;

    public Cases(String path, Theme dt, Theme lt)
    {
        darkTheme = dt;
        lightTheme = lt;
        userPrivateDataCase = new Case<>(new UserData(null, null, null), new ORM<UserPrivateData>()
        {
            public Map write(UserPrivateData data)
            {
                Map map = new HashMap();
                map.put("userId", data.userId());
                map.put("userToken", data.userToken());
                map.put("refreshToken", data.refreshToken());
                return map;
            }
            public UserPrivateData read(Map map)
            {
                return new UserData((String)map.get("userId"), (String)map.get("userToken"), (String)map.get("refreshToken"));
            }
        }, path + "/userPrivateDataCase");
        syncDataCase = new Case<>(new SynchronizationData(-1, null), new ORM<SyncData>()
        {
            public Map write(SyncData syncData)
            {
                Map map = new HashMap();
                map.put("lastSyncTime", syncData.lastSyncTime());
                map.put("hash", syncData.hash());
                return map;
            }
            public SyncData read(Map map)
            {
                return new SynchronizationData((Long)map.get("lastSyncTime"), (String)map.get("hash"));
            }
        }, path + "/syncDataCase");
        versionsCase = new Case<>(InitData.create(VersionsData.create(-1, -1)), new ORM<Init<Versions>>()
        {
            public Map write(Init<Versions> versions)
            {
                Map map = new HashMap();
                map.put("init", versions.init());
                map.put("version", versions.data().version());
                map.put("currencies", versions.data().currencies());
                return map;
            }
            public Init<Versions> read(Map map)
            {
                return InitData.create((Boolean)map.get("init"), VersionsData.create((Long)map.get("version"),
                        (Long)map.get("currencies")));
            }
        }, path + "/versionsCase");
        themeCase = new Case<>(lightTheme, new ORM<Theme>()
        {
            public Map write(Theme theme)
            {
                Map map = new HashMap();
                map.put("dark_state", theme.isDarkTheme());
                map.put("color_background", theme.colors().background());
                map.put("color_foreground", theme.colors().foreground());
                map.put("color_accent", theme.colors().accent());
                map.put("color_positive", theme.colors().positive());
                map.put("color_neutral", theme.colors().neutral());
                map.put("color_negative", theme.colors().negative());
                map.put("color_alert", theme.colors().alert());
                map.put("color_confirm", theme.colors().confirm());
                return map;
            }
            public Theme read(Map map)
            {
                return new ThemeData((Boolean)map.get("dark_state"), new ColorsData(((Long)map.get("color_background")).intValue(),
                        ((Long)map.get("color_foreground")).intValue(),
                        ((Long)map.get("color_accent")).intValue(),
                        ((Long)map.get("color_positive")).intValue(),
                        ((Long)map.get("color_neutral")).intValue(),
                        ((Long)map.get("color_negative")).intValue(),
                        ((Long)map.get("color_alert")).intValue(),
                        ((Long)map.get("color_confirm")).intValue()));
            }
        }, path + "/themeCase");
        userInfoCase = new Case<>(InitData.create(UserInfoData.create(null, null, null, -1)), new ORM<Init<UserInfo>>()
        {
            public Map write(Init<UserInfo> info)
            {
                Map map = new HashMap();
                map.put("init", info.init());
                map.put("name", info.data().name());
                map.put("avatar", info.data().avatar());
                map.put("birthDate", info.data().birthDate());
                map.put("gender", info.data().gender() != null ? info.data().gender().name() : null);
                return map;
            }
            public Init<UserInfo> read(Map map)
            {
                String genderType = (String)map.get("gender");
                return InitData.create((Boolean)map.get("init"),
                        UserInfoData.create((String)map.get("name"),
                                genderType != null ? UserInfo.GenderType.valueOf(genderType) : null,
                                (String)map.get("avatar"),
                                (Long)map.get("birthDate")));
            }
        }, path + "/userInfoCase");
    }

    public Init<Versions> getVersions()
    {
        return versionsCase.get();
    }
    public void setVersions(Init<Versions> versions)
    {
        versionsCase.save(versions);
    }

    public UserPrivateData getUserPrivateData()
    {
        return userPrivateDataCase.get();
    }
    public void login(UserPrivateData data)
    {
        userPrivateDataCase.save(data);
    }
    public void logout()
    {
        userPrivateDataCase.clear();
        syncDataCase.clear();
        userInfoCase.clear();
    }

    public Init<UserInfo> getUserInfo()
    {
        return userInfoCase.get();
    }
    public void setUserInfo(Init<UserInfo> info)
    {
        userInfoCase.save(info);
    }

    public SyncData getSyncData()
    {
        return syncDataCase.get();
    }
    public void setSyncData(SyncData data)
    {
        syncDataCase.save(data);
    }

    public Theme getCurrentTheme()
    {
        return themeCase.get();
    }
    public void setDarkTheme()
    {
        themeCase.save(darkTheme);
    }
    public void setLightTheme()
    {
        themeCase.save(lightTheme);
    }
}