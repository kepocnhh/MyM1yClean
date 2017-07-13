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

public class Auth
        implements AuthApi
{
    static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final JsonConverter jsonConverter;

    public Auth(OkHttpClient c, JsonConverter j)
    {
        client = c;
        jsonConverter = j;
    }

    public UserPrivateData postLogin(final UserSecretData data)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, UnknownError
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
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                try
                {
                    return jsonConverter.getUserPrivateData(response.body().string());
                }
                catch(Exception e)
                {
                    throw new UnknownError();
                }
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new UnknownError();
        }
    }
    public UserPrivateData postRegistration(final UserSecretData data)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, UnknownError
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
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                try
                {
                    return jsonConverter.getUserPrivateData(response.body().string());
                }
                catch(Exception e)
                {
                    throw new UnknownError();
                }
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new UnknownError();
        }
    }
    public UserPrivateData postRefreshToken(final String refreshToken)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, UnknownError
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
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                try
                {
                    return jsonConverter.getUserPrivateDataAfterRefresh(response.body().string());
                }
                catch(Exception e)
                {
                    throw new UnknownError();
                }
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new UnknownError();
        }
    }
}