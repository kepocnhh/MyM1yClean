package stan.mym1y.clean.modules.settings;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.work.SettingsContract;
import stan.mym1y.clean.cores.users.UserInfo;

class SettingsModel
    implements SettingsContract.Model
{
    private final Settings settings;

    SettingsModel(Settings settings)
    {
        this.settings = settings;
    }

    public UserInfo getUserInfo()
    {
        return settings.getUserInfo().data();
    }
}