package stan.mym1y.clean.modules.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.work.MenuContract;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class MenuFragment
    extends UtilFragment
{
    static public UtilFragment newInstance(MenuContract.Behaviour b)
    {
        MenuFragment fragment = new MenuFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private MenuContract.Presenter presenter;
    private final MenuContract.View view = new MenuContract.View()
    {
        public void update(final MenuContract.Screen screen)
        {
            behaviour.screen(screen);
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    changeCurrentScreen(screen);
                }
            });
        }
        public void update(final UserInfo info)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    name.setText(info.name());
                }
            });
        }
    };
    private MenuContract.Behaviour behaviour;

    private View background;
    private TextView name;
    private View user_info_divider;
    private TextView transactions_text;
    private TextView settings_text;
    private View logout_divider;
    private ImageView logout_icon;
    private TextView logout_text;

    private Theme currentTheme;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.transactions:
                log("try move to transactions");
                presenter.screen(MenuContract.Screen.TRANSACTIONS);
                break;
            case R.id.settings:
                log("try move to settings");
                presenter.screen(MenuContract.Screen.SETTINGS);
                break;
            case R.id.logout:
                log("try logout");
                behaviour.logout();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.menu_screen;
    }
    protected void initViews(View v)
    {
        background = findView(R.id.background);
        name = findView(R.id.name);
        user_info_divider = findView(R.id.user_info_divider);
        transactions_text = findView(R.id.transactions_text);
        settings_text = findView(R.id.settings_text);
        logout_divider = findView(R.id.logout_divider);
        logout_icon = findView(R.id.logout_icon);
        logout_text = findView(R.id.logout_text);
        setClickListener(findView(R.id.logout), findView(R.id.transactions), findView(R.id.settings));
    }
    protected void init()
    {
        setTheme(App.component().themeSwitcher().theme());
        presenter = new MenuPresenter(view, new MenuModel(App.component().settings()));
        presenter.update();
        presenter.screen(MenuContract.Screen.TRANSACTIONS);
    }

    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        background.setBackgroundColor(theme.colors().background());
        name.setTextColor(theme.colors().foreground());
        user_info_divider.setBackgroundColor(theme.colors().foreground());
        transactions_text.setTextColor(theme.colors().foreground());
        settings_text.setTextColor(theme.colors().foreground());
        logout_divider.setBackgroundColor(theme.colors().foreground());
        logout_icon.setColorFilter(theme.colors().foreground());
        logout_text.setTextColor(theme.colors().foreground());
    }

    private void changeCurrentScreen(MenuContract.Screen screen)
    {
        transactions_text.setTextColor(currentTheme.colors().foreground());
        settings_text.setTextColor(currentTheme.colors().foreground());
        switch(screen)
        {
            case TRANSACTIONS:
                transactions_text.setTextColor(currentTheme.colors().accent());
                break;
            case SETTINGS:
                settings_text.setTextColor(currentTheme.colors().accent());
                break;
            case USER_INFO:
                break;
            default:
                throw new RuntimeException("screen " + screen + " not implemented!");
        }
    }
}