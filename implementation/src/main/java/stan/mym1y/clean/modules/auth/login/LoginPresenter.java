package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.Observer;

class LoginPresenter
    extends ModelPresenter<LoginContract.View, LoginContract.Model>
    implements LoginContract.Presenter
{
    private final Observer<String> tokenObserver = new Observer<String>()
    {
        public void next(String token)
        {
            getView().success(token);
        }
        @Override
        public void error(Throwable t)
        {
            if(t instanceof AuthContract.NetworkErrorException)
            {
                getView().error((AuthContract.NetworkErrorException)t);
            }
            else if(t instanceof AuthContract.UnauthorizedException)
            {
                getView().error((AuthContract.UnauthorizedException)t);
            }
            else if(t instanceof AuthContract.InvalidDataException)
            {
                getView().error((AuthContract.InvalidDataException)t);
            }
            else if(t instanceof AuthContract.ServerErrorException)
            {
                getView().error((AuthContract.ServerErrorException)t);
            }
            else
            {
                getView().error(new AuthContract.UnknownErrorException());
            }
        }
        @Override
        public void complete()
        {
        }
    };

    LoginPresenter(LoginContract.View v, LoginContract.Model m)
    {
        super(v, m);
    }

    @Override
    public void login(final String login, final String password)
    {
        try
        {
            getModel().checkData(login, password);
            onNewThread(new Runnable()
            {
                @Override
                public void run()
                {
                    getModel().login(login, password).subscribe(tokenObserver);
                }
            });
        }
        catch(LoginContract.ValidateDataException e)
        {
            getView().error(e);
        }
    }
}