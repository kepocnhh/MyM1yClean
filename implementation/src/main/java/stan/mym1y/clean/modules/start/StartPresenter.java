package stan.mym1y.clean.modules.start;

import java.util.List;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.StartContract;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.single.SingleObserver;

class StartPresenter
    extends ModelPresenter<StartContract.View, StartContract.Model>
    implements StartContract.Presenter
{
    private final SingleObserver<Versions> versionsObserver = new SingleObserver<Versions>()
    {
        public void success(final Versions actualVersions)
        {
            Versions cacheVersions = model().getCacheVersions();
            if(actualVersions.version() != cacheVersions.version())
            {
                log("versions need update");
                NotifyObservable observable = null;
                if(actualVersions.currencies() != cacheVersions.currencies())
                {
                    log("currencies need update");
                    observable = model().updateCurrencies();
                }
                else
                {
                    log("currencies still actual");
                }
                if(observable == null)
                {
                    view().error();
                    return;
                }
                observable.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        model().update(actualVersions);
                        view().complete();
                    }
                    public void error(Throwable t)
                    {
                        view().error(new StartContract.DataNeedUpdateException());
                    }
                });
            }
            else
            {
                log("versions still actual");
                view().complete();
            }
        }
        public void error(Throwable t)
        {
            try
            {
                throw t;
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
            catch(Throwable throwable)
            {
                view().error();
            }
        }
    };

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
                model().getActualVersions().subscribe(versionsObserver);
            }
        });
    }
}