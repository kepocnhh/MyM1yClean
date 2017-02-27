package stan.mym1y.clean.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stan.json.JSONParser;
import stan.json.JSONWriter;
import stan.mym1y.clean.connection.API;
import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.di.JsonConverter;
import stan.mym1y.clean.modules.sync.SynchronizationData;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.modules.users.UserData;

public class Converter
    implements JsonConverter
{
    @Override
    public UserPrivateData parseUserPrivateData(String json)
    {
        Map responseBody = (Map)JSONParser.read(json);
        return new UserData((String)responseBody.get("localId"), (String)responseBody.get("idToken"), (String)responseBody.get("refreshToken"));
    }
    @Override
    public SyncData parseSyncData(String json)
    {
        Map responseMap = (Map)JSONParser.read(json);
        return new SynchronizationData((Long)responseMap.get("lastSyncTime"), (String)responseMap.get("hash"));
    }
    @Override
    public List<TransactionModel> parseTransactions(String json)
    {
        List list = (List)JSONParser.read(json);
        List<TransactionModel> listTransactions = new ArrayList<>(list.size());
        for(Object object : list)
        {
            Map map = (Map)object;
            listTransactions.add(new Transaction(
                    ((Long)map.get("id")).intValue()
                    ,((Long)map.get("count")).intValue()
                    ,(Long)map.get("date")
            ));
        }
        return listTransactions;
    }

    @Override
    public String convertAuthBody(String login, String password)
    {
        return JSONWriter.write(API.Auth.Params.getAuthBody(login, password));
    }
    @Override
    public String convertSyncBody(SyncData syncData)
    {
        return JSONWriter.write(API.Transactions.getSyncBody(syncData));
    }
    @Override
    public String convertRefreshTokenBody(String refreshToken)
    {
        return JSONWriter.write(API.Auth.Params.getRefreshTokenBody(refreshToken));
    }

    @Override
    public String convertTransactions(ListModel<TransactionModel> listModel)
    {
        List list = new ArrayList(listModel.size());
        for(int i=0; i<listModel.size(); i++)
        {
            Map map = new HashMap();
            map.put("count", listModel.get(i).getCount());
            map.put("id", listModel.get(i).getId());
            map.put("date", listModel.get(i).getDate());
            list.add(map);
        }
        return JSONWriter.write(list);
    }
}