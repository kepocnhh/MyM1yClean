package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.mvp.ModelRouterPresenter;

class GeneralPresenter
    extends ModelRouterPresenter<GeneralContract.View, GeneralContract.Model, GeneralContract.Router>
    implements GeneralContract.Presenter
{
    GeneralPresenter(GeneralContract.View v, GeneralContract.Model m, GeneralContract.Router r)
    {
        super(v, m, r);
    }

    @Override
    public void checkAuth()
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    getRouter().toMain(getModel().getUserPrivateData());
                }
                catch(GeneralContract.UserNotAuthorizedException e)
                {
                    getRouter().toAuth();
                }
            }
        });
    }

    @Override
    public void enter(final UserPrivateData data)
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().login(data);
                getRouter().toMain(data);
            }
        });
    }

    @Override
    public void logout()
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                getModel().logout();
                getModel().clearTransactions();
                getRouter().toAuth();
            }
        });
    }
}