package stan.mym1y.clean.modules.menu;

import stan.mym1y.clean.contracts.work.MenuContract;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class MenuPresenter
    extends ModelPresenter<MenuContract.View, MenuContract.Model>
    implements MenuContract.Presenter
{
    private MenuContract.Screen currentScreen;

    MenuPresenter(MenuContract.View v, MenuContract.Model m)
    {
        super(v, m);
    }

    public void screen(MenuContract.Screen screen)
    {
        if(currentScreen != screen)
        {
            currentScreen = screen;
            view().update(screen);
        }
    }
    public void update()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                view().update(model().getUserInfo());
            }
        });
    }
}