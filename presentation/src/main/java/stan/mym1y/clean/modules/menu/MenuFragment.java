package stan.mym1y.clean.modules.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.MenuContract;
import stan.mym1y.clean.cores.ui.Theme;
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

    private MenuContract.Behaviour behaviour;

    private View logout_divider;
    private ImageView logout_icon;
    private TextView logout_text;

    private Theme currentTheme;

    protected void onClickView(int id)
    {
        switch(id)
        {
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
        logout_divider = findView(R.id.logout_divider);
        logout_icon = findView(R.id.logout_icon);
        logout_text = findView(R.id.logout_text);
        setClickListener(findView(R.id.logout));
    }
    protected void init()
    {
        setTheme(App.component().themeSwitcher().theme());
    }

    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        logout_divider.setBackgroundColor(theme.colors().foreground());
        logout_icon.setColorFilter(theme.colors().foreground());
        logout_text.setTextColor(theme.colors().foreground());
    }
}