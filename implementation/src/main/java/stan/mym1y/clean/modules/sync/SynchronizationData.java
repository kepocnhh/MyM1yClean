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

    @Override
    public long getLastSyncTime()
    {
        return lastSyncTime;
    }
    @Override
    public String getHash()
    {
        return hash;
    }
}