package stan.mym1y.clean.connection;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stan.mym1y.clean.di.Connection;
import stan.reactive.Observable;
import stan.reactive.SimpleObservable;

public class OkHttp
    implements Connection
{
    static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public Observable<String> get(final String url)
    {
        return new SimpleObservable<String>()
        {
            public String work()
                    throws IOException
            {
                return client.newCall(new Request.Builder().url(url).build()).execute().body().string();
            }
        };
    }
    @Override
    public Observable<String> get(final String url, final Map<String, String> params)
    {
        return new SimpleObservable<String>()
        {
            public String work()
                    throws IOException
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                for(String key : params.keySet())
                {
                    urlBuilder.addQueryParameter(key, params.get(key));
                }
                return client.newCall(new Request.Builder().url(urlBuilder.build()).build()).execute().body().string();
            }
        };
    }

    @Override
    public Observable<String> post(final String url, final String body)
    {
        return new SimpleObservable<String>()
        {
            @Override
            protected String work()
                    throws IOException
            {
                return client.newCall(new Request.Builder().url(url).post(RequestBody.create(JSON, body)).build()).execute().body().string();
            }
        };
    }
    @Override
    public Observable<Answer> post(final String url, final Map<String, String> params, final String body)
    {
        return new SimpleObservable<Answer>()
        {
            @Override
            protected Answer work()
                    throws IOException
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                for(String key : params.keySet())
                {
                    urlBuilder.addQueryParameter(key, params.get(key));
                }
                Request.Builder builder = new Request.Builder().url(urlBuilder.build()).post(RequestBody.create(JSON, body));
                Response response = client.newCall(builder.build()).execute();
                return new Answer(response.body().string(), response.code());
            }
        };
    }
}