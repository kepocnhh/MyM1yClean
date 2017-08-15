package stan.mym1y.clean.contracts.work;

import stan.mym1y.clean.cores.users.UserInfo;

public interface SettingsContract
{
    interface Model
    {
        UserInfo getUserInfo();
    }
    interface View
    {
        void update(UserInfo info);
    }
    interface Presenter
    {
        void update();
    }
}