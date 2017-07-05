package stan.mym1y.clean.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import stan.mym1y.clean.data.remote.Connection;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.data.remote.apis.GlobalDataApi;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;
import stan.mym1y.clean.okhttp.apis.Auth;
import stan.mym1y.clean.okhttp.apis.GlobalData;
import stan.mym1y.clean.okhttp.apis.PrivateData;

public class OkHttp
        implements Connection
{
    private final AuthApi authApi;
    private final PrivateDataApi privateDataApi;
    private final GlobalDataApi globalDataApi;

    public OkHttp(JsonConverter jc)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        authApi = new Auth(client, jc);
        privateDataApi = new PrivateData(client, jc);
        globalDataApi = new GlobalData(client, jc);
    }

    public AuthApi authApi()
    {
        return authApi;
    }
    public PrivateDataApi privateDataApi()
    {
        return privateDataApi;
    }
    public GlobalDataApi globalDataApi()
    {
        return globalDataApi;
    }
}