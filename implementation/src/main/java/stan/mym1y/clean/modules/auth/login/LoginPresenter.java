package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.modules.users.UserSecret;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class LoginPresenter
    extends ModelPresenter<LoginContract.View, LoginContract.Model>
    implements LoginContract.Presenter
{
    LoginPresenter(LoginContract.View v, LoginContract.Model m)
    {
        super(v, m);
    }

    public void login(final String login, final String password)
    {
        log("login...");
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().checkData(login, password);
                }
                catch(LoginContract.ValidateDataException e)
                {
                    view().error(e);
                    return;
                }
                try
                {
                    view().success(model().login(new UserSecret(login, password)));
                }
                catch(ErrorsContract.NetworkException e)
                {
                    view().error(e);
                }
                catch(ErrorsContract.UnauthorizedException e)
                {
                    view().error(e);
                }
                catch(UnknownError e)
                {
                    view().error();
                }
            }
        });
    }
}