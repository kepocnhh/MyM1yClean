package stan.mym1y.clean.boxes;

import java.util.HashMap;
import java.util.Map;

import stan.boxes.Case;
import stan.boxes.ORM;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.users.UserData;

public class Cases
        implements Settings
{
    private final Case<UserPrivateData> userPrivateDataCase;
    private final Case<SyncData> syncDataCase;

    public Cases(String path)
    {
        userPrivateDataCase = new Case<>(new UserData(null, null, null), new ORM<UserPrivateData>()
        {
            @Override
            public Map write(UserPrivateData data)
            {
                Map map = new HashMap();
                map.put("userId", data.getUserId());
                map.put("userToken", data.getUserToken());
                map.put("refreshToken", data.getRefreshToken());
                return map;
            }
            @Override
            public UserPrivateData read(Map map)
            {
                return new UserData((String)map.get("userId"), (String)map.get("userToken"), (String)map.get("refreshToken"));
            }
        }, path + "/userPrivateDataCase");
        syncDataCase = new Case<>(new SynchronizationData(-1, null), new ORM<SyncData>()
        {
            @Override
            public Map write(SyncData syncData)
            {
                Map map = new HashMap();
                map.put("lastSyncTime", syncData.getLastSyncTime());
                map.put("hash", syncData.getHash());
                return map;
            }
            @Override
            public SyncData read(Map map)
            {
                return new SynchronizationData((Long)map.get("lastSyncTime"), (String)map.get("hash"));
            }
        }, path + "/syncDataCase");
    }

    @Override
    public UserPrivateData getUserPrivateData()
    {
        return userPrivateDataCase.get();
    }
    @Override
    public void login(UserPrivateData userPrivateData)
    {
        userPrivateDataCase.save(userPrivateData);
    }
    @Override
    public void logout()
    {
        userPrivateDataCase.clear();
        syncDataCase.clear();
    }

    @Override
    public SyncData getSyncData()
    {
        return syncDataCase.get();
    }
    @Override
    public void setSyncData(SyncData data)
    {
        syncDataCase.save(data);
    }
}