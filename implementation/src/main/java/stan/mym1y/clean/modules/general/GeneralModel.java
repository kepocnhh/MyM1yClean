package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.di.Settings;

class GeneralModel
    implements GeneralContract.Model
{
    private Models.Transactions transactions;
    private Settings settings;

    GeneralModel(Models.Transactions ts, Settings sttngs)
    {
        transactions = ts;
        settings = sttngs;
    }

    @Override
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

    @Override
    public void login(UserPrivateData data)
    {
        settings.login(data);
    }

    @Override
    public void logout()
    {
        settings.logout();
    }

    @Override
    public void clearTransactions()
    {
        transactions.clear();
    }
}