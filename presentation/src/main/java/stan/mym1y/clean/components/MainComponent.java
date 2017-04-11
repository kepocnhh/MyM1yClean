package stan.mym1y.clean.components;

import stan.mym1y.clean.data.local.DAO;
import stan.mym1y.clean.data.remote.Connection;

public class MainComponent
    implements AppComponent
{
    private final DAO dataAccess;
    private final Connection connection;
    private final JsonConverter jsonConverter;
    private final FoldersAccess foldersAccess;
    private final Settings settings;
    private final Security security;

    public MainComponent(DAO d, Connection c, JsonConverter j, FoldersAccess f, Settings ss, Security scr)
    {
        dataAccess = d;
        connection = c;
        jsonConverter = j;
        foldersAccess = f;
        settings = ss;
        security = scr;
    }

    public DAO dataLocal()
    {
        return dataAccess;
    }
    public Connection dataRemote()
    {
        return connection;
    }
    public JsonConverter jsonConverter()
    {
        return jsonConverter;
    }
    public FoldersAccess foldersAccess()
    {
        return foldersAccess;
    }
    public Settings settings()
    {
        return settings;
    }
    public Security security()
    {
        return security;
    }
}