package stan.mym1y.clean.okhttp.apis;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.data.remote.apis.AuthApi;
import stan.mym1y.clean.components.JsonConverter;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

public class Auth
    implements AuthApi
{
    static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final JsonConverter jsonConverter;

    public Auth(OkHttpClient clnt, JsonConverter jc)
    {
        client = clnt;
        jsonConverter = jc;
    }

    public SingleObservable<UserPrivateData> postLogin(final UserSecretData data)
    {
        return new SingleObservable<UserPrivateData>()
        {
            public void subscribe(SingleObserver<UserPrivateData> o)
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Post.LOGIN).newBuilder();
                urlBuilder.addQueryParameter("key", SERVER_KEY);
                Response response;
                try
                {
                    response = client.newCall(new Request.Builder()
                            .url(urlBuilder.build())
                            .post(RequestBody.create(JSON, jsonConverter.get(data)))
                            .build()).execute();
                }
                catch(Throwable t)
                {
                    o.error(new ErrorsContract.NetworkException(urlBuilder.build().toString()));
                    return;
                }
                switch(response.code())
                {
                    case Codes.SUCCESS:
                        try
                        {
                            o.success(jsonConverter.getUserPrivateData(response.body().string()));
                        }
                        catch(Exception e)
                        {
                            o.error(new UnknownError());
                        }
                        break;
                    case Codes.UNAUTHORIZED:
                        try
                        {
                            o.error(new ErrorsContract.UnauthorizedException());
                        }
                        catch(Exception e)
                        {
                            o.error(new UnknownError());
                        }
                        break;
                    default:
                        o.error(new UnknownError());
                }
            }
        };
    }
    public SingleObservable<UserPrivateData> postRegistration(final UserSecretData data)
    {
        return new SingleObservable<UserPrivateData>()
        {
            public void subscribe(SingleObserver<UserPrivateData> o)
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Post.REGISTRATION).newBuilder();
                urlBuilder.addQueryParameter("key", SERVER_KEY);
                Response response;
                try
                {
                    response = client.newCall(new Request.Builder()
                            .url(urlBuilder.build())
                            .post(RequestBody.create(JSON, jsonConverter.get(data)))
                            .build()).execute();
                }
                catch(Throwable t)
                {
                    o.error(new ErrorsContract.NetworkException(urlBuilder.build().toString()));
                    return;
                }
                switch(response.code())
                {
                    case Codes.SUCCESS:
                        try
                        {
                            o.success(jsonConverter.getUserPrivateData(response.body().string()));
                        }
                        catch(Exception e)
                        {
                            o.error(new UnknownError());
                        }
                        break;
                    case Codes.UNAUTHORIZED:
                        try
                        {
                            o.error(new ErrorsContract.UnauthorizedException());
                        }
                        catch(Exception e)
                        {
                            o.error(new UnknownError());
                        }
                        break;
                    default:
                        o.error(new UnknownError());
                }
            }
        };
    }
    public SingleObservable<UserPrivateData> postRefreshToken(final String refreshToken)
    {
        return new SingleObservable<UserPrivateData>()
        {
            public void subscribe(SingleObserver<UserPrivateData> o)
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Post.REFRESH_TOKEN).newBuilder();
                urlBuilder.addQueryParameter("key", SERVER_KEY);
                Response response;
                try
                {
                    response = client.newCall(new Request.Builder()
                            .url(urlBuilder.build())
                            .post(RequestBody.create(JSON, jsonConverter.getRefreshTokenBody(refreshToken)))
                            .build()).execute();
                }
                catch(Throwable t)
                {
                    o.error(new ErrorsContract.NetworkException(urlBuilder.build().toString()));
                    return;
                }
                switch(response.code())
                {
                    case Codes.SUCCESS:
                        try
                        {
                            o.success(jsonConverter.getUserPrivateDataAfterRefresh(response.body().string()));
                        }
                        catch(Exception e)
                        {
                            o.error(new UnknownError());
                        }
                        break;
                    case Codes.UNAUTHORIZED:
                        try
                        {
                            o.error(new ErrorsContract.UnauthorizedException());
                        }
                        catch(Exception e)
                        {
                            o.error(new UnknownError());
                        }
                        break;
                    default:
                        o.error(new UnknownError());
                }
            }
        };
    }
}