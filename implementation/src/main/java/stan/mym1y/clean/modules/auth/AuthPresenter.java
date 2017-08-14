package stan.mym1y.clean.modules.auth;

import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.units.mvp.RouterPresenter;

class AuthPresenter
        extends RouterPresenter<AuthContract.View, AuthContract.Router>
        implements AuthContract.Presenter
{
    private AuthContract.Screen currentScreen;

    AuthPresenter(AuthContract.View v, AuthContract.Router r)
    {
        super(v, r);
    }

    public void toLogin()
    {
        if(currentScreen != AuthContract.Screen.LOGIN)
        {
            currentScreen = AuthContract.Screen.LOGIN;
            router().toLogin();
        }
    }
    public void toLogin(Providers.Type type)
    {
        if(currentScreen != AuthContract.Screen.LOGIN_PROVIDER)
        {
            currentScreen = AuthContract.Screen.LOGIN_PROVIDER;
            router().toLogin(type);
        }
    }
    public void toRegistration()
    {
        if(currentScreen != AuthContract.Screen.REGISTRATION)
        {
            currentScreen = AuthContract.Screen.REGISTRATION;
            router().toRegistration();
        }
    }
}