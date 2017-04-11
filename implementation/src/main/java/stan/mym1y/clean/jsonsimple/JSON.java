package stan.mym1y.clean.jsonsimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
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
        Map<String, Object> map = new HashMap<>();
        map.put("email", data.getLogin());
        map.put("password", data.getPassword());
        map.put("returnSecureToken", true);
        return JSONWriter.write(map);
    }
    public String getRefreshTokenBody(String refreshToken)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);
        return JSONWriter.write(map);
    }
    public String get(List<Transaction> transactions)
    {
        List<Map> list = new ArrayList<>();
        for(Transaction transaction : transactions)
        {
            Map<String, Object> map = new HashMap<>();
            map.put("id", transaction.getId());
            map.put("count", transaction.getCount());
            map.put("date", transaction.getDate());
            list.add(map);
        }
        return JSONWriter.write(list);
    }
    public String get(SyncData data)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("hash", data.getHash());
        map.put("lastSyncTime", data.getLastSyncTime());
        return JSONWriter.write(map);
    }

    public UserPrivateData getUserPrivateData(String string)
            throws ParseException
    {
        try
        {
            Map map = (Map)JSONParser.read(string);
            return new UserData((String)map.get("localId"),
                    (String)map.get("idToken"),
                    (String)map.get("refreshToken"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    public UserPrivateData getUserPrivateDataAfterRefresh(String string)
            throws ParseException
    {
        try
        {
            Map map = (Map)JSONParser.read(string);
            return new UserData((String)map.get("user_id"),
                    (String)map.get("access_token"),
                    (String)map.get("refresh_token"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
    public List<Transaction> getTransactions(String string)
            throws ParseException
    {
        List<Transaction> transactions = new ArrayList<>();
        try
        {
            List list = (List)JSONParser.read(string);
            for(Object o : list)
            {
                Map map = (Map)o;
                transactions.add(new TransactionData(((Long)map.get("id")).intValue(),
                        ((Long)map.get("count")).intValue(),
                        (Long)map.get("date")));
            }
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
        return transactions;
    }
    public SyncData getSyncData(String string)
            throws ParseException
    {
        try
        {
            Map map = (Map)JSONParser.read(string);
            return new SynchronizationData((Long)map.get("lastSyncTime"),
                    (String)map.get("hash"));
        }
        catch(Throwable t)
        {
            throw new ParseException(t);
        }
    }
}