package stan.mym1y.clean.modules.auth.provider;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginWithProviderContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class LoginWithProviderPresenter
    extends ModelPresenter<LoginWithProviderContract.View, LoginWithProviderContract.Model>
    implements LoginWithProviderContract.Presenter
{
    LoginWithProviderPresenter(LoginWithProviderContract.View v, LoginWithProviderContract.Model m)
    {
        super(v, m);
    }

    public void login(final Providers.Type type, final String code)
    {
        log("login...");
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    view().success(model().login(model().getToken(code), type));
                }
                catch(ErrorsContract.NetworkException e)
                {
                    view().error(e);
                }
                catch(ErrorsContract.UnauthorizedException e)
                {
                    view().error(e);
                }
                catch(ErrorsContract.UnknownException e)
                {
                    view().error();
                }
            }
        });
    }
}