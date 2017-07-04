package stan.mym1y.clean.okhttp.apis;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.remote.apis.DataApi;
import stan.mym1y.clean.components.JsonConverter;
import stan.reactive.functions.Action;
import stan.reactive.functions.Worker;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public class Data
    implements DataApi
{
    static private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final JsonConverter jsonConverter;

    public Data(OkHttpClient c, JsonConverter j)
    {
        client = c;
        jsonConverter = j;
    }

    public SingleObservable<Versions> getVersions()
    {
        return SingleObservable.create(new Worker<Versions>()
        {
            public Versions work()
                    throws Throwable
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
                                return jsonConverter.getVersions(json);
                            }
                            catch(Exception e)
                            {
                                throw new UnknownError();
                            }
                        }
                    default:
                        throw new UnknownError();
                }
            }
        });
    }
    public SingleObservable<List<Currency>> getCurrencies()
    {
        return SingleObservable.create(new Worker<List<Currency>>()
        {
            public List<Currency> work()
                    throws Throwable
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
                                return jsonConverter.getCurrencies(json);
                            }
                            catch(Exception e)
                            {
                                throw new UnknownError();
                            }
                        }
                    default:
                        throw new UnknownError();
                }
            }
        });
    }
    public SingleObservable<List<CashAccountRequest>> getTransactions(final UserPrivateData data)
    {
        return SingleObservable.create(new Worker<List<CashAccountRequest>>()
        {
            public List<CashAccountRequest> work()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.CASH_ACCOUNTS + "/" +data.userId()+ ".json").newBuilder();
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
        });
    }
    public SingleObservable<SyncData> getSyncData(final UserPrivateData data)
    {
        return SingleObservable.create(new Worker<SyncData>()
        {
            public SyncData work()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.SYNC + "/" +data.userId()+ ".json").newBuilder();
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
                                return jsonConverter.getSyncData(json);
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
        });
    }
    public NotifyObservable putTransactions(final UserPrivateData data, final CashAccountRequest cashAccountRequest)
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.CASH_ACCOUNTS + "/" +data.userId()+ "/" + cashAccountRequest.cashAccount().uuid() + ".json").newBuilder();
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
                        throw new UnknownError();
                }
            }
        });
    }
    public NotifyObservable putTransactions(final UserPrivateData data, final List<CashAccountRequest> cashAccountRequests)
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.CASH_ACCOUNTS + "/" +data.userId()+ ".json").newBuilder();
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
                        throw new UnknownError();
                }
            }
        });
    }
    public NotifyObservable putSyncData(final UserPrivateData data, final SyncData syncData)
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.SYNC + "/" +data.userId()+ ".json").newBuilder();
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
                        throw new UnknownError();
                }
            }
        });
    }
}