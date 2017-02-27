package stan.mym1y.clean.modules.auth.login;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.single.SingleObserver;

class LoginPresenter
    extends ModelPresenter<LoginContract.View, LoginContract.Model>
    implements LoginContract.Presenter
{
    private final SingleObserver<UserPrivateData> userPrivateDataObserver = new SingleObserver<UserPrivateData>()
    {
        @Override
        public void success(UserPrivateData data)
        {
            log("login success");
            getView().success(data);
        }
        @Override
        public void error(Throwable t)
        {
            log("login error: " + t.getMessage());
            try
            {
                throw t;
            }
            catch(ErrorsContract.NetworkErrorException e)
            {
                getView().error(e);
            }
            catch(ErrorsContract.UnauthorizedException e)
            {
                getView().error(e);
            }
            catch(ErrorsContract.InvalidDataException e)
            {
                getView().error(e);
            }
            catch(ErrorsContract.ServerErrorException e)
            {
                getView().error(e);
            }
            catch(ErrorsContract.UnknownErrorException e)
            {
                getView().error(e);
            }
            catch(Throwable throwable)
            {
                getView().error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nerror " + t.getMessage()));
            }
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
                    getModel().login(login, password).subscribe(userPrivateDataObserver);
                }
                catch(LoginContract.ValidateDataException e)
                {
                    getView().error(e);
                }
            }
        });
    }
}