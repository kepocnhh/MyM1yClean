package stan.mym1y.clean.modules.start;

import java.util.List;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.StartContract;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.local.models.CurrenciesModels;
import stan.mym1y.clean.data.remote.apis.GlobalDataApi;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

class StartModel
    implements StartContract.Model
{
    private final Settings settings;
    private final CurrenciesModels.Currencies currencies;
    private final GlobalDataApi globalDataApi;

    StartModel(Settings ss, CurrenciesModels.Currencies cs, GlobalDataApi gda)
    {
        settings = ss;
        currencies = cs;
        globalDataApi = gda;
    }

    public SingleObservable<Versions> getActualVersions()
    {
        return globalDataApi.getVersions();
    }
    public Versions getCacheVersions()
    {
        return settings.getVersions();
    }
    public void update(Versions versions)
    {
        settings.setVersions(versions);
    }
    public NotifyObservable updateCurrencies()
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                globalDataApi.getCurrencies().subscribe(new SingleObserver<List<Currency>>()
                {
                    public void success(List<Currency> cs)
                    {
                        currencies.clear();
                        currencies.add(cs);
                        o.notice();
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
}