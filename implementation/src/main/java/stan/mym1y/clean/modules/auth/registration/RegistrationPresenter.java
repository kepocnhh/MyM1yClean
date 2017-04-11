package stan.mym1y.clean.modules.auth.registration;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.users.UserSecret;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.single.SingleObserver;

class RegistrationPresenter
        extends ModelPresenter<RegistrationContract.View, RegistrationContract.Model>
        implements RegistrationContract.Presenter
{
    private final SingleObserver<UserPrivateData> loginObserver = new SingleObserver<UserPrivateData>()
    {
        public void success(UserPrivateData data)
        {
            view().success(data);
        }
        public void error(Throwable t)
        {
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

    RegistrationPresenter(RegistrationContract.View v, RegistrationContract.Model m)
    {
        super(v, m);
    }

    public void registration(final String login, final String password)
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    model().checkData(login, password);
                }
                catch(RegistrationContract.ValidateDataException e)
                {
                    view().error(e);
                    return;
                }
                model().login(new UserSecret(login, password)).subscribe(loginObserver);
            }
        });
    }
}