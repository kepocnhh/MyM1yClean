package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.components.Settings;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.data.local.models.TransactionsModels;

class GeneralModel
    implements GeneralContract.Model
{
    private final CashAccountsModels.CashAccounts cashAccounts;
    private final TransactionsModels.Transactions transactions;
    private final Settings settings;

    GeneralModel(CashAccountsModels.CashAccounts ca, TransactionsModels.Transactions t, Settings sttngs)
    {
        cashAccounts = ca;
        transactions = t;
        settings = sttngs;
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