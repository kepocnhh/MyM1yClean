package stan.mym1y.clean.cases;

import java.util.HashMap;
import java.util.Map;

import stan.boxes.Case;
import stan.boxes.ORM;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.ui.ColorsData;
import stan.mym1y.clean.modules.ui.ThemeData;
import stan.mym1y.clean.modules.users.UserData;
import stan.mym1y.clean.modules.versions.VersionsData;

public class Cases
    implements Settings
{
    private final Case<Versions> versionsCase;
    private final Case<UserPrivateData> userPrivateDataCase;
    private final Case<SyncData> syncDataCase;
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
        versionsCase = new Case<>(new VersionsData(false, -1, -1), new ORM<Versions>()
        {
            public Map write(Versions versions)
            {
                Map map = new HashMap();
                map.put("init", versions.init());
                map.put("version", versions.version());
                map.put("currencies", versions.currencies());
                return map;
            }
            public Versions read(Map map)
            {
                return new VersionsData((Boolean)map.get("init"),
                        (Long)map.get("version"),
                        (Long)map.get("currencies"));
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
    }

    public Versions getVersions()
    {
        return versionsCase.get();
    }
    public void setVersions(Versions versions)
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