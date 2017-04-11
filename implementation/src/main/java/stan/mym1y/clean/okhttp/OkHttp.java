package stan.mym1y.clean.okhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import stan.mym1y.clean.data.remote.Connection;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.data.remote.apis.DataApi;
import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.okhttp.apis.Auth;
import stan.mym1y.clean.okhttp.apis.Data;

public class OkHttp
        implements Connection
{
    private final AuthApi authApi;
    private final DataApi dataApi;

    public OkHttp(JsonConverter jc)
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        authApi = new Auth(client, jc);
        dataApi = new Data(client, jc);
    }

    public AuthApi authApi()
    {
        return authApi;
    }
    public DataApi dataApi()
    {
        return dataApi;
    }
}