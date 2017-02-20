package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.di.Settings;

class GeneralModel
    implements GeneralContract.Model
{
    private Settings settings;

    GeneralModel(Settings sttngs)
    {
        settings = sttngs;
    }

    @Override
    public String getToken()
            throws GeneralContract.UserNotAuthorizedException
    {
        String token = settings.getUserToken();
        if(token == null || token.length() == 0)
        {
            throw new GeneralContract.UserNotAuthorizedException();
        }
        return token;
    }
}