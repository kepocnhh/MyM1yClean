package stan.mym1y.clean.cores.sync;

public interface SyncData
{
    long lastSyncTime();
    String hash();
}