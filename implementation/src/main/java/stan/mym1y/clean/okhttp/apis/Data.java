package stan.mym1y.clean.okhttp.apis;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.remote.apis.DataApi;
import stan.mym1y.clean.components.JsonConverter;
import stan.reactive.functions.Action;
import stan.reactive.functions.Worker;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
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

    public SingleObservable<List<Transaction>> getTransactions(final UserPrivateData data)
    {
        return SingleObservable.create(new Worker<List<Transaction>>()
        {
            public List<Transaction> work()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.TRANSACTIONS + "/" +data.getUserId()+ ".json").newBuilder();
                urlBuilder.addQueryParameter("auth", data.getUserToken());
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
                        try
                        {
                            return jsonConverter.getTransactions(response.body().string());
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
        });
    }
    public SingleObservable<SyncData> getSyncData(final UserPrivateData data)
    {
        return SingleObservable.create(new Worker<SyncData>()
        {
            public SyncData work()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.SYNC + "/" +data.getUserId()+ ".json").newBuilder();
                urlBuilder.addQueryParameter("auth", data.getUserToken());
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
    public NotifyObservable putTransactions(final UserPrivateData data, final List<Transaction> transactions)
    {
        return new NotifyObservable()
        {
            public void subscribe(NotifyObserver o)
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.TRANSACTIONS + "/" +data.getUserId()+ ".json").newBuilder();
                urlBuilder.addQueryParameter("auth", data.getUserToken());
                Response response;
                try
                {
                    response = client.newCall(new Request.Builder()
                            .url(urlBuilder.build())
                            .put(RequestBody.create(JSON, jsonConverter.get(transactions)))
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
                            o.notice();
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
    public NotifyObservable putSyncData(final UserPrivateData data, final SyncData syncData)
    {
        return NotifyObservable.create(new Action()
        {
            public void run()
                    throws Throwable
            {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(Get.SYNC + "/" +data.getUserId()+ ".json").newBuilder();
                urlBuilder.addQueryParameter("auth", data.getUserToken());
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