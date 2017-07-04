package stan.mym1y.clean.cases;

import java.util.HashMap;
import java.util.Map;

import stan.boxes.Case;
import stan.boxes.ORM;
import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.users.UserData;
import stan.mym1y.clean.modules.versions.VersionsData;

public class Cases
    implements Settings
{
    private final Case<Versions> versionsCase;
    private final Case<UserPrivateData> userPrivateDataCase;
    private final Case<SyncData> syncDataCase;

    public Cases(String path)
    {
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
        }, path + "versionsCase");
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
}