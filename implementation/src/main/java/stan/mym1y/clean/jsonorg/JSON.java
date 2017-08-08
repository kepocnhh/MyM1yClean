package stan.mym1y.clean.jsonorg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserProviderData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.modules.cashaccounts.CashAccountData;
import stan.mym1y.clean.modules.currencies.CurrencyData;
import stan.mym1y.clean.modules.network.requests.CashAccountRequestData;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.TransactionData;
import stan.mym1y.clean.modules.users.UserData;
import stan.mym1y.clean.modules.users.UserProvider;
import stan.mym1y.clean.modules.versions.VersionsData;

public class JSON
        implements JsonConverter
{
    public String get(UserSecretData data)
    {
        try
        {
            return new JSONObject().put("email", data.login())
                                   .put("password", data.password())
                                   .put("returnSecureToken", true).toString();
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
    public String get(UserProviderData data, Providers.Type type)
    {
        try
        {
            return new JSONObject().put("postBody", "id_token="+data.tokenId()+"&providerId="+type.value)
                                   .put("requestUri", "http://localhost")
                                   .put("returnIdpCredential", true)
                                   .put("returnSecureToken", true).toString();
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
    public String getRefreshTokenBody(String refreshToken)
    {
        try
        {
            return new JSONObject().put("grant_type", "refresh_token")
                                   .put("refresh_token", refreshToken).toString();
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
    public String get(List<Transaction> transactions)
    {
        return getJSONArray(transactions).toString();
    }
    private JSONArray getJSONArray(List<Transaction> transactions)
    {
        JSONArray array = new JSONArray();
        try
        {
            for(Transaction transaction : transactions)
            {
                array.put(get(transaction));
            }
            return array;
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
    private JSONObject get(Transaction transaction)
            throws JSONException
    {
        return new JSONObject().put("id", transaction.id())
//                               .put("cashAccountId", transaction.cashAccountId())
                               .put("income", transaction.income())
                               .put("count", transaction.count())
                               .put("minorCount", transaction.minorCount())
                               .put("date", transaction.date());
    }
    public String get(SyncData syncData)
    {
        try
        {
            return new JSONObject().put("hash", syncData.hash())
                                   .put("lastSyncTime", syncData.lastSyncTime()).toString();
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
    public String get(CashAccountRequest cashAccountRequest)
    {
        return getCashAccountRequest(cashAccountRequest).toString();
    }
    private JSONObject getCashAccountRequest(CashAccountRequest cashAccountRequest)
    {
        JSONObject object = get(cashAccountRequest.cashAccount());
        try
        {
            object.put("transactions", getJSONArray(cashAccountRequest.transactions()));
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
        return object;
    }
    public String getCashAccountRequests(List<CashAccountRequest> cashAccountRequests)
    {
        JSONObject object = new JSONObject();
        try
        {
            for(CashAccountRequest cashAccountRequest: cashAccountRequests)
            {
                object.put(cashAccountRequest.cashAccount().uuid(), getCashAccountRequest(cashAccountRequest));
            }
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
        return object.toString();
    }

    public Versions getVersions(String json)
            throws ParseException
    {
        try
        {
            JSONObject object = new JSONObject(json);
            return new VersionsData(true, object.getLong("version"),
                    object.getLong("currencies"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    public List<Currency> getCurrencies(String json)
            throws ParseException
    {
        try
        {
            JSONArray array = new JSONArray(json);
            if(array.length() == 0)
            {
                return Collections.emptyList();
            }
            List<Currency> currencies = new ArrayList<>(array.length());
            for(int i=0; i<array.length(); i++)
            {
                currencies.add(getCurrency(array.getJSONObject(i)));
            }
            return currencies;
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    private Currency getCurrency(JSONObject object)
            throws JSONException
    {
        return new CurrencyData(object.getString("codeName"),
                object.getString("codeNumber"),
                object.getString("name"),
                Currency.MinorUnitType.valueOf(object.getString("minorUnitType")));
    }

    private JSONObject get(CashAccount cashAccount)
    {
        try
        {
            return new JSONObject().put("id", cashAccount.id())
                                   .put("currencyCodeNumber", cashAccount.currencyCodeNumber())
                                   .put("title", cashAccount.title());
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }
    }

    public UserPrivateData getUserPrivateData(String json)
            throws ParseException
    {
        try
        {
            JSONObject object = new JSONObject(json);
            return new UserData(object.getString("localId"),
                    object.getString("idToken"),
                    object.getString("refreshToken"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    public UserPrivateData getUserPrivateDataAfterRefresh(String json)
            throws ParseException
    {
        try
        {
            JSONObject object = new JSONObject(json);
            return new UserData(object.getString("user_id"),
                    object.getString("access_token"),
                    object.getString("refresh_token"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    public UserProviderData getUserProviderData(String json)
            throws ParseException
    {
        try
        {
            JSONObject object = new JSONObject(json);
            return new UserProvider(object.getString("id_token"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    private List<Transaction> getTransactions(JSONArray array, long cashAccountId)
            throws JSONException
    {
        if(array.length() == 0)
        {
            return Collections.emptyList();
        }
        List<Transaction> transactions = new ArrayList<>(array.length());
        for(int i=0; i<array.length(); i++)
        {
            transactions.add(getTransaction(array.getJSONObject(i), cashAccountId));
        }
        return transactions;
    }
    private Transaction getTransaction(JSONObject object, long cashAccountId)
            throws JSONException
    {
        return new TransactionData(object.getLong("id"),
//                object.getLong("cashAccountId"),
                cashAccountId,
                object.getLong("date"),
                object.getBoolean("income"),
                object.getInt("count"),
                object.getInt("minorCount"));
    }
    public SyncData getSyncData(String json)
            throws ParseException
    {
        try
        {
            JSONObject object = new JSONObject(json);
            return new SynchronizationData(object.getLong("lastSyncTime"),
                    object.getString("hash"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    public List<CashAccountRequest> getCashAccounts(String json)
            throws ParseException
    {
        try
        {
            List<CashAccountRequest> cashAccountRequests = new ArrayList<>();
            JSONObject object = new JSONObject(json);
            Iterator<String> iterator = object.keys();
            while(iterator.hasNext())
            {
                String uuid = iterator.next();
                cashAccountRequests.add(getCashAccountRequest(object.getJSONObject(uuid), uuid));
            }
            return cashAccountRequests;
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    private CashAccountRequest getCashAccountRequest(JSONObject object, String uuid)
            throws JSONException
    {
        JSONArray transactions = object.optJSONArray("transactions");
        if(transactions == null)
        {
            return new CashAccountRequestData(getCashAccount(object, uuid), Collections.<Transaction>emptyList());
        }
        else
        {
            return new CashAccountRequestData(getCashAccount(object, uuid), getTransactions(transactions, object.getLong("id")));
        }
    }
    private CashAccount getCashAccount(JSONObject object, String uuid)
            throws JSONException
    {
        return new CashAccountData(object.getLong("id"), uuid, object.getString("currencyCodeNumber"), object.getString("title"));
    }
}