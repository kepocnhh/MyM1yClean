package stan.mym1y.clean.modules.auth.registration;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.modules.users.UserSecret;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class RegistrationPresenter
        extends ModelPresenter<RegistrationContract.View, RegistrationContract.Model>
        implements RegistrationContract.Presenter
{
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
                try
                {
                    view().success(model().registration(new UserSecret(login, password)));
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