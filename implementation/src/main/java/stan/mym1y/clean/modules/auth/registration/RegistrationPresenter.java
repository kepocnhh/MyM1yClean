package stan.mym1y.clean.modules.auth.registration;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.Observer;

class RegistrationPresenter
        extends ModelPresenter<RegistrationContract.View, RegistrationContract.Model>
        implements RegistrationContract.Presenter
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
            try
            {
                throw t;
            }
            catch(ErrorsContract.NetworkErrorException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.UnauthorizedException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.InvalidDataException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.ServerErrorException exception)
            {
                getView().error(exception);
            }
            catch(ErrorsContract.UnknownErrorException exception)
            {
                getView().error(exception);
            }
            catch(Throwable throwable)
            {
                getView().error(new ErrorsContract.UnknownErrorException(getClass().getName() + "\nerror " + throwable.getMessage()));
            }
        }
        @Override
        public void complete()
        {
        }
    };

    RegistrationPresenter(RegistrationContract.View v, RegistrationContract.Model m)
    {
        super(v, m);
    }

    @Override
    public void registration(final String login, final String password)
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
                catch(RegistrationContract.ValidateDataException e)
                {
                    getView().error(e);
                }
            }
        });
    }
}