package stan.mym1y.clean.modules.start;

import java.util.List;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.StartContract;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.local.models.CurrenciesModels;
import stan.mym1y.clean.data.remote.apis.GlobalDataApi;

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

    public Versions getActualVersions()
            throws ErrorsContract.NetworkException, ErrorsContract.UnknownException
    {
        try
        {
            return globalDataApi.getVersions();
        }
        catch(ErrorsContract.DataNotExistException e)
        {
            throw new ErrorsContract.UnknownException(e);
        }
    }
    public Versions getCacheVersions()
    {
        return settings.getVersions();
    }
    public void update(Versions versions)
    {
        settings.setVersions(versions);
    }
    public void updateCurrencies()
            throws ErrorsContract.UnknownException
    {
        try
        {
            List<Currency> list = globalDataApi.getCurrencies();
            currencies.clear();
            currencies.add(list);
        }
        catch(ErrorsContract.NetworkException | ErrorsContract.DataNotExistException e)
        {
            throw new ErrorsContract.UnknownException(e);
        }
    }
}