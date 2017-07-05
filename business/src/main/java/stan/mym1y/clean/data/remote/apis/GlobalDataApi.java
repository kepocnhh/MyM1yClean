package stan.mym1y.clean.data.remote.apis;

import java.util.List;

import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.remote.Connection;
import stan.reactive.single.SingleObservable;

public interface GlobalDataApi
{
    String BASE_URL = Connection.BASE_DATA_URL + "/global/";

    interface Get
    {
        String VERSIONS = BASE_URL + "versions.json";
        String CURRENCIES = BASE_URL + "currencies.json";
    }

    interface Codes
    {
        int SUCCESS = 200;
    }

    SingleObservable<Versions> getVersions();
    SingleObservable<List<Currency>> getCurrencies();
}