package stan.mym1y.clean.modules.work;

import android.view.View;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.contracts.MenuContract;
import stan.mym1y.clean.contracts.WorkContract;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.modules.main.MainFragment;
import stan.mym1y.clean.modules.menu.MenuFragment;
import stan.mym1y.clean.units.fragments.UtilFragment;
import stan.mym1y.clean.units.views.DrawerContainer;

public class WorkFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(WorkContract.Behaviour b)
    {
        WorkFragment fragment = new WorkFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private WorkContract.Behaviour behaviour;

    private DrawerContainer drawer_container;

    private Theme currentTheme;

    protected int getContentView()
    {
        return R.layout.work_screen;
    }
    protected void initViews(View v)
    {
        drawer_container = findView(R.id.drawer_container);
    }
    protected void init()
    {
        drawer_container.setIosStyle(true);
        drawer_container.setIosOffset(2);
        drawer_container.setEdge(false);
        drawer_container.setPadSize(px(56));
        drawer_container.setEdgePadSize(px(16));
        drawer_container.setSpeedFactor(2);
        drawer_container.setTweaking(5);
        drawer_container.setScaleStyle(false);
        drawer_container.setScrimFactor(1.5f);
        drawer_container.setDividerWidth(px(1));
        replace(R.id.main_frame, MainFragment.newInstance(new MainContract.Behaviour()
        {
            public void unauthorized()
            {
                behaviour.logout();
            }
        }));
        replace(R.id.menu_frame, MenuFragment.newInstance(new MenuContract.Behaviour()
        {
            public void logout()
            {
                behaviour.logout();
            }
        }));
        setTheme(App.component().themeSwitcher().theme());
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        setStatusBarColor(theme.colors().background());
        setSystemUiVisibilityLight(!theme.isDarkTheme());
        setNavigationBarColor(theme.isDarkTheme() ? theme.colors().background() : theme.colors().foreground());
        drawer_container.setScrimColor(theme.colors().background());
        drawer_container.setDividerColor(theme.colors().foreground());
//        drawer_container.setScrimColor(theme.colors().accent());
        drawer_container.setBackgroundColor(theme.colors().background());
    }
}