package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.cores.versions.Versions;
import stan.mym1y.clean.data.Init;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.data.local.models.TransactionsModels;
import stan.mym1y.clean.data.remote.apis.GlobalDataApi;

class GeneralModel
    implements GeneralContract.Model
{
    private final CashAccountsModels.CashAccounts cashAccounts;
    private final TransactionsModels.Transactions transactions;
    private final Settings settings;
    private final GlobalDataApi globalDataApi;

    GeneralModel(CashAccountsModels.CashAccounts ca, TransactionsModels.Transactions t, Settings sttngs, GlobalDataApi gda)
    {
        cashAccounts = ca;
        transactions = t;
        settings = sttngs;
        globalDataApi = gda;
    }

    public Versions getActualVersions()
            throws ErrorsContract.UnknownException
    {
        try
        {
            return globalDataApi.getVersions();
        }
        catch(ErrorsContract.NetworkException | ErrorsContract.DataNotExistException e)
        {
            throw new ErrorsContract.UnknownException(e);
        }
    }
    public Init<Versions> getCacheVersions()
    {
        return settings.getVersions();
    }
    public UserPrivateData getUserPrivateData()
            throws GeneralContract.UserNotAuthorizedException
    {
        UserPrivateData data = settings.getUserPrivateData();
        if(data == null
                || data.userId() == null || data.userId().length() == 0
                || data.userToken() == null || data.userToken().length() == 0)
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
    public void clearAll()
    {
        transactions.clear();
        cashAccounts.clear();
    }
}