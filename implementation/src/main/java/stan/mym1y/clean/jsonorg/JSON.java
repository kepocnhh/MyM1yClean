package stan.mym1y.clean.jsonorg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.users.UserSecretData;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.TransactionData;
import stan.mym1y.clean.modules.users.UserData;

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
        JSONArray array = new JSONArray();
        try
        {
            for(Transaction transaction : transactions)
            {
                array.put(get(transaction));
            }
            return array.toString();
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
                               .put("cashAccountId", transaction.cashAccountId())
                               .put("count", transaction.count())
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
    public List<Transaction> getTransactions(String json)
            throws ParseException
    {
        try
        {
            JSONArray array = new JSONArray(json);
            if(array.length() == 0)
            {
                return Collections.emptyList();
            }
            List<Transaction> transactions = new ArrayList<>(array.length());
            for(int i=0; i<array.length(); i++)
            {
                transactions.add(getTransaction(array.getJSONObject(i)));
            }
            return transactions;
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    private Transaction getTransaction(JSONObject object)
            throws JSONException
    {
        return new TransactionData(object.getLong("id"),
                object.getLong("cashAccountId"),
                object.getLong("date"),
                object.getInt("count"));
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
}