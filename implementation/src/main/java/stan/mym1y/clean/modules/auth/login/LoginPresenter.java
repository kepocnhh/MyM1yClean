package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.users.UserSecret;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.single.SingleObserver;

class LoginPresenter
    extends ModelPresenter<LoginContract.View, LoginContract.Model>
    implements LoginContract.Presenter
{
    private final SingleObserver<UserPrivateData> loginObserver = new SingleObserver<UserPrivateData>()
    {
        public void success(UserPrivateData data)
        {
            view().success(data);
        }
        public void error(Throwable t)
        {
//            log("error " + t.toString());
            try
            {
                throw t;
            }
            catch(ErrorsContract.UnauthorizedException e)
            {
                view().error(e);
            }
            catch(ErrorsContract.NetworkException e)
            {
                view().error(e);
            }
            catch(Throwable throwable)
            {
                view().error();
            }
        }
    };

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
                model().login(new UserSecret(login, password)).subscribe(loginObserver);
            }
        });
    }
}