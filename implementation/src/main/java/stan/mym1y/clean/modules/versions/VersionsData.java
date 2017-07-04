package stan.mym1y.clean.modules.versions;

import stan.mym1y.clean.cores.versions.Versions;

public class VersionsData
    implements Versions
{
    private final boolean init;
    private final long version;
    private final long currencies;

    public VersionsData(long v, long cs)
    {
        this(true, v, cs);
    }
    public VersionsData(boolean it, long v, long cs)
    {
        init = it;
        version = v;
        currencies = cs;
    }

    public boolean init()
    {
        return init;
    }
    public long version()
    {
        return version;
    }
    public long currencies()
    {
        return currencies;
    }
}