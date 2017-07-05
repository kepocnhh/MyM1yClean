package stan.mym1y.clean.data.remote;

import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.GlobalDataApi;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;

public interface Connection
{
    String BASE_DATA_URL = "https://mym1yclean.firebaseio.com/";

    AuthApi authApi();
    PrivateDataApi privateDataApi();
    GlobalDataApi globalDataApi();
}