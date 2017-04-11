package stan.mym1y.clean.modules.auth;

import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.units.mvp.RouterPresenter;

class AuthPresenter
        extends RouterPresenter<AuthContract.View, AuthContract.Router>
        implements AuthContract.Presenter
{
    private int screen;

    AuthPresenter(AuthContract.View v, AuthContract.Router r)
    {
        super(v, r);
        screen = -1;
    }

    public void toLogin()
    {
        if(screen != 1)
        {
            screen = 1;
            router().toLogin();
        }
    }
    public void toRegistration()
    {
        if(screen != 2)
        {
            screen = 2;
            router().toRegistration();
        }
    }
}