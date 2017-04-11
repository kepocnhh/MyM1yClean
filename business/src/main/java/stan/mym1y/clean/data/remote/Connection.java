package stan.mym1y.clean.data.remote;

import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.DataApi;

public interface Connection
{
    AuthApi authApi();
    DataApi dataApi();
}