package stan.mym1y.clean.okhttp.apis;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.remote.apis.GlobalDataApi;

public class GlobalData
    implements GlobalDataApi
{
    private final OkHttpClient client;
    private final JsonConverter jsonConverter;

    public GlobalData(OkHttpClient c, JsonConverter j)
    {
        client = c;
        jsonConverter = j;
    }

    public Versions getVersions()
            throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.VERSIONS).newBuilder();
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
                        return jsonConverter.getVersions(json);
                    }
                    catch(Exception e)
                    {
                        throw new ErrorsContract.UnknownException("invalid response data: " + json);
                    }
                }
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
    public List<Currency> getCurrencies()
            throws ErrorsContract.NetworkException, ErrorsContract.DataNotExistException, ErrorsContract.UnknownException
    {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.CURRENCIES).newBuilder();
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
                        return jsonConverter.getCurrencies(json);
                    }
                    catch(Exception e)
                    {
                        throw new ErrorsContract.UnknownException("invalid response data: " + json);
                    }
                }
            default:
                throw new ErrorsContract.UnknownException("unknown response code " + response.code());
        }
    }
}