package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.local.models.TransactionsModels;

class GeneralModel
    implements GeneralContract.Model
{
    private TransactionsModels.Transactions transactions;
    private Settings settings;

    GeneralModel(TransactionsModels.Transactions t, Settings sttngs)
    {
        transactions = t;
        settings = sttngs;
    }

    public UserPrivateData getUserPrivateData()
            throws GeneralContract.UserNotAuthorizedException
    {
        UserPrivateData data = settings.getUserPrivateData();
        if(data == null
                || data.getUserId() == null || data.getUserId().length() == 0
                || data.getUserToken() == null || data.getUserToken().length() == 0)
        {
            throw new GeneralContract.UserNotAuthorizedException();
        }
        return data;
    }

    public void login(UserPrivateData data)
    {
        settings.login(data);
    }
    public void logout()
    {
        settings.logout();
    }
    public void clearTransactions()
    {
        transactions.clear();
    }
}