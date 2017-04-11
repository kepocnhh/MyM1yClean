package stan.mym1y.clean.modules.sync;

import stan.mym1y.clean.cores.sync.SyncData;

public class SynchronizationData
    implements SyncData
{
    private long lastSyncTime;
    private String hash;

    public SynchronizationData(long lst, String h)
    {
        lastSyncTime = lst;
        hash = h;
    }

    public long getLastSyncTime()
    {
        return lastSyncTime;
    }
    public String getHash()
    {
        return hash;
    }
}