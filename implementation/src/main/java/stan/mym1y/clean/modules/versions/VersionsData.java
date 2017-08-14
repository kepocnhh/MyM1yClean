package stan.mym1y.clean.modules.versions;

import stan.mym1y.clean.cores.versions.Versions;

public class VersionsData
    implements Versions
{
    static public Versions create(long v, long cs)
    {
        return new VersionsData(v, cs);
    }

    private final long version;
    private final long currencies;

    private VersionsData(long v, long cs)
    {
        version = v;
        currencies = cs;
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