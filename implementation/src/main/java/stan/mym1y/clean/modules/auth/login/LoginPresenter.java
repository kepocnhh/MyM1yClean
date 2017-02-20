package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class LoginPresenter
    extends ModelPresenter<LoginContract.View, LoginContract.Model>
    implements LoginContract.Presenter
{
    LoginPresenter(LoginContract.View v, LoginContract.Model m)
    {
        super(v, m);
    }

    @Override
    public void login(String login, String password)
    {
        try
        {
            getModel().checkData(login, password);
        }
        catch(LoginContract.ValidateDataException e)
        {
            getView().error(e);
        }
    }
}