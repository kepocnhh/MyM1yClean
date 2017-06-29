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

    public void checkAuth()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().getUserPrivateData();
                }
                catch(GeneralContract.UserNotAuthorizedException e)
                {
                    log("User Not Authorized!");
                    router().toAuth();
                    return;
                }
                router().toMain();
            }
        });
    }
    public void enter(final UserPrivateData data)
    {
        log("enter...");
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().login(data);
                router().toMain();
            }
        });
    }
    public void logout()
    {
        log("logout...");
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().logout();
                model().clearAll();
                router().toAuth();
            }
        });
    }
}