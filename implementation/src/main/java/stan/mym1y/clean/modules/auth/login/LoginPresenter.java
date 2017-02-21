package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.Observer;

class LoginPresenter
    extends ModelPresenter<LoginContract.View, LoginContract.Model>
    implements LoginContract.Presenter
{
    private final Observer<UserPrivateData> tokenObserver = new Observer<UserPrivateData>()
    {
        public void next(UserPrivateData token)
        {
            getView().success(token);
        }
        @Override
        public void error(Throwable t)
        {
            if(t instanceof ErrorsContract.NetworkErrorException)
            {
                getView().error((ErrorsContract.NetworkErrorException)t);
            }
            else if(t instanceof ErrorsContract.UnauthorizedException)
            {
                getView().error((ErrorsContract.UnauthorizedException)t);
            }
            else if(t instanceof ErrorsContract.InvalidDataException)
            {
                getView().error((ErrorsContract.InvalidDataException)t);
            }
            else if(t instanceof ErrorsContract.ServerErrorException)
            {
                getView().error((ErrorsContract.ServerErrorException)t);
            }
            else
            {
                getView().error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nerror " + t.getMessage()));
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
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    getModel().checkData(login, password);
                    getModel().login(login, password).subscribe(tokenObserver);
                }
                catch(LoginContract.ValidateDataException e)
                {
                    getView().error(e);
                }
            }
        });
    }
}