package stan.mym1y.clean.connection;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stan.mym1y.clean.di.Connection;
import stan.reactive.single.SingleObservable;

public class OkHttp
    implements Connection
{
    static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    @Override
    public SingleObservable<Answer> get(final String url, final Map<String, String> params)
    {
        return new SingleObservable.Work<Answer>()
        {
            public Answer work()
                    throws IOException
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                for(String key : params.keySet())
                {
                    urlBuilder.addQueryParameter(key, params.get(key));
                }
                Response response = client.newCall(new Request.Builder().url(urlBuilder.build()).build()).execute();
                return new Answer(response.body().string(), response.code());
            }
        };
    }

    @Override
    public SingleObservable<Answer> post(final String url, final Map<String, String> params, final String body)
    {
        return new SingleObservable.Work<Answer>()
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

    @Override
    public SingleObservable<Answer> put(final String url, final Map<String, String> params, final String body)
    {
        return new SingleObservable.Work<Answer>()
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
                Request.Builder builder = new Request.Builder().url(urlBuilder.build()).put(RequestBody.create(JSON, body));
                Response response = client.newCall(builder.build()).execute();
                return new Answer(response.body().string(), response.code());
            }
        };
    }
}