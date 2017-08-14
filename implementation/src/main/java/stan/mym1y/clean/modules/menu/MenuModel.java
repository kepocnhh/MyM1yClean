package stan.mym1y.clean.modules.menu;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.work.MenuContract;
import stan.mym1y.clean.cores.users.UserInfo;

class MenuModel
    implements MenuContract.Model
{
    private final Settings settings;

    public MenuModel(Settings ss)
    {
        settings = ss;
    }

    public UserInfo getUserInfo()
    {
        return settings.getUserInfo().data();
    }
}