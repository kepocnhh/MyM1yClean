package stan.mym1y.clean.okhttp.apis;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.remote.apis.PrivateDataApi;

public class PrivateData
        implements PrivateDataApi
{
    static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final JsonConverter jsonConverter;

    public PrivateData(OkHttpClient c, JsonConverter j)
    {
        client = c;
        jsonConverter = j;
    }

    public List<CashAccountRequest> getTransactions(final UserPrivateData data)
            throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getCashAccountsUrl(data)).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                String json;
                try
                {
                    json = response.body().string();
                }
                catch(Exception e)
                {
                    throw new UnknownError();
                }
                if(json == null)
                {
                    throw new UnknownError();
                }
                if(json.equals("null"))
                {
                    throw new ErrorsContract.DataNotExistException();
                }
                else
                {
                    try
                    {
                        return jsonConverter.getCashAccounts(json);
                    }
                    catch(Exception e)
                    {
                        throw new UnknownError();
                    }
                }
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new UnknownError();
        }
    }
    public SyncData getSyncData(final UserPrivateData data)
            throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getSyncUrl(data)).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                String json;
                try
                {
                    json = response.body().string();
                }
                catch(Exception e)
                {
                    throw new ErrorsContract.UnknownException(e);
                }
                if(json.equals("null"))
                {
                    throw new ErrorsContract.DataNotExistException();
                }
                else
                {
                    try
                    {
                        return jsonConverter.getSyncData(json);
                    }
                    catch(Exception e)
                    {
                        throw new ErrorsContract.UnknownException(e);
                    }
                }
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
    public void putTransactions(final UserPrivateData data, final CashAccountRequest cashAccountRequest)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getCashAccountUrl(data, cashAccountRequest.cashAccount())).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .put(RequestBody.create(JSON, jsonConverter.get(cashAccountRequest)))
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                break;
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
    public void putTransactions(final UserPrivateData data, final List<CashAccountRequest> cashAccountRequests)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getCashAccountsUrl(data)).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .put(RequestBody.create(JSON, jsonConverter.getCashAccountRequests(cashAccountRequests)))
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                break;
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
    public void putSyncData(final UserPrivateData data, final SyncData syncData)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getSyncUrl(data)).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .put(RequestBody.create(JSON, jsonConverter.get(syncData)))
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                break;
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
    public UserInfo getUserInfo(UserPrivateData data)
            throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getUserInfoUrl(data)).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                String json;
                try
                {
                    json = response.body().string();
                }
                catch(Exception e)
                {
                    throw new ErrorsContract.UnknownException(e);
                }
                if(json.equals("null"))
                {
                    throw new ErrorsContract.DataNotExistException();
                }
                else
                {
                    try
                    {
                        return jsonConverter.getUserInfo(json);
                    }
                    catch(Exception e)
                    {
                        throw new ErrorsContract.UnknownException(e);
                    }
                }
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
    public void putUserInfo(UserPrivateData data, UserInfo info)
            throws ErrorsContract.NetworkException, ErrorsContract.UnauthorizedException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.getUserInfoUrl(data)).newBuilder();
        urlBuilder.addQueryParameter("auth", data.userToken());
        Response response;
        try
        {
            response = client.newCall(new Request.Builder()
                    .url(urlBuilder.build())
                    .put(RequestBody.create(JSON, jsonConverter.get(info)))
                    .build()).execute();
        }
        catch(Throwable t)
        {
            throw new ErrorsContract.NetworkException(urlBuilder.build().toString());
        }
        switch(response.code())
        {
            case Codes.SUCCESS:
                break;
            case Codes.UNAUTHORIZED:
                throw new ErrorsContract.UnauthorizedException();
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
}