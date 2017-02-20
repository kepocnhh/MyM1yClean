package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.auth.LoginContract;

class LoginModel
    implements LoginContract.Model
{
    @Override
    public void checkData(String login, String password)
            throws LoginContract.ValidateDataException
    {
        if(login == null || login.length() == 0
                || password == null || password.length() == 0)
        {
            throw new LoginContract.ValidateDataException();
        }
    }
}
