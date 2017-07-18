package stan.mym1y.clean.components;

import stan.mym1y.clean.data.local.DAO;
import stan.mym1y.clean.data.remote.Connection;

public interface AppComponent
{
    DAO dataLocal();
    Connection dataRemote();
    JsonConverter jsonConverter();
    FoldersAccess foldersAccess();
    Settings settings();
    Security security();
    ThemeSwitcher themeSwitcher();
}