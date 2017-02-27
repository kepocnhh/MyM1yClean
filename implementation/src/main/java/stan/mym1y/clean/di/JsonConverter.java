package stan.mym1y.clean.di;

import java.util.List;

import stan.mym1y.clean.cores.sync.SyncData;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;

public interface JsonConverter
{
    UserPrivateData parseUserPrivateData(String json);
    SyncData parseSyncData(String json);
    List<TransactionModel> parseTransactions(String json);

    String convertAuthBody(String login, String password);
    String convertSyncBody(SyncData syncData);
    String convertRefreshTokenBody(String refreshToken);
    String convertTransactions(ListModel<TransactionModel> listModel);
}