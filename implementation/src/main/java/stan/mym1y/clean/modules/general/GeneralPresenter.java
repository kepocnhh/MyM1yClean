package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.units.mvp.ModelRouterPresenter;

class GeneralPresenter
    extends ModelRouterPresenter<GeneralContract.View, GeneralContract.Model, GeneralContract.Router>
    implements GeneralContract.Presenter
{
    GeneralPresenter(GeneralContract.View v, GeneralContract.Model m, GeneralContract.Router r)
    {
        super(v, m, r);
    }

    public void start()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                final Versions cacheVersions = model().getCacheVersions();
                if(!cacheVersions.init())
                {
                    router().toStart();
                    return;
                }
                try
                {
                    Versions actualVersions = model().getActualVersions();
                    if(actualVersions.version() != cacheVersions.version())
                    {
                        router().toStart();
                    }
                    else
                    {
                        checkAuth();
                    }
                }
                catch(ErrorsContract.UnknownException e)
                {
                    router().toStart();
                }
            }
        });
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
                    router().toWork();
                }
                catch(GeneralContract.UserNotAuthorizedException e)
                {
                    log("User Not Authorized!");
                    router().toAuth();
                }
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
                router().toWork();
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