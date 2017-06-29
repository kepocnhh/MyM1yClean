package stan.mym1y.clean.modules.sync;

import stan.mym1y.clean.cores.sync.SyncData;

public class SynchronizationData
    implements SyncData
{
    private final long lastSyncTime;
    private final String hash;

    public SynchronizationData(long lst, String h)
    {
        lastSyncTime = lst;
        hash = h;
    }

    public long lastSyncTime()
    {
        return lastSyncTime;
    }
    public String hash()
    {
        return hash;
    }
}