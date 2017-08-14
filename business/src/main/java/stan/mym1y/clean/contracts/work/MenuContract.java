package stan.mym1y.clean.contracts.work;

import stan.mym1y.clean.cores.users.UserInfo;

public interface MenuContract
{
    interface Model
    {
        UserInfo getUserInfo();
    }
    interface View
    {
        void update(Screen screen);
        void update(UserInfo info);
    }
    interface Presenter
    {
        void screen(Screen screen);
        void update();
    }

    interface Behaviour
    {
        void screen(Screen screen);
        void logout();
    }

    enum Screen
    {
        TRANSACTIONS,
        SETTINGS,
        USER_INFO,
    }
}