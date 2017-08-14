package stan.mym1y.clean.modules.start;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.StartContract;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.Init;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class StartPresenter
    extends ModelPresenter<StartContract.View, StartContract.Model>
    implements StartContract.Presenter
{
    StartPresenter(StartContract.View v, StartContract.Model m)
    {
        super(v, m);
    }

    public void checkSync()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    checkVersions(model().getActualVersions());
                }
                catch(ErrorsContract.NetworkException e)
                {
                    if(model().getCacheVersions().init())
                    {
                        view().complete();
                    }
                    else
                    {
                        view().error(new StartContract.CantContinueWithoutDataException());
                    }
                }
                catch(ErrorsContract.UnknownException e)
                {
                    view().error();
                }
            }
        });
    }
    private void checkVersions(Versions actualVersions)
    {
        Init<Versions> cacheVersions = model().getCacheVersions();
        if(actualVersions.version() != cacheVersions.data().version())
        {
            log("versions need update");
            checkVersions(actualVersions, cacheVersions.data());
        }
        else
        {
            log("versions still actual");
            view().complete();
        }
    }
    private void checkVersions(Versions actualVersions, Versions cacheVersions)
    {
        if(actualVersions.currencies() != cacheVersions.currencies())
        {
            log("currencies need update");
            try
            {
                model().updateCurrencies();
            }
            catch(ErrorsContract.UnknownException e)
            {
                view().error(new StartContract.DataNeedUpdateException());
                return;
            }
        }
        else
        {
            log("currencies still actual");
        }
        model().update(actualVersions);
        view().complete();
    }
}