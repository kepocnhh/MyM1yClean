package stan.mym1y.clean.modules.menu;

import android.view.View;

import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.MenuContract;
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

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.logout:
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
        setClickListener(findView(R.id.logout));
    }
    protected void init()
    {
    }
}