package stan.mym1y.clean.modules.auth;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.mvp.ModelRouterPresenter;

class AuthPresenter
        extends ModelRouterPresenter<AuthContract.View, AuthContract.Model, AuthContract.Router>
        implements AuthContract.Presenter
{
    private AuthContract.Screen currentScreen;

    AuthPresenter(AuthContract.View v, AuthContract.Model m, AuthContract.Router r)
    {
        super(v, m, r);
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
    public void enter(UserPrivateData data)
    {
        try
        {
            model().checkUserInfo(data);
            view().success(data);
        }
        catch(AuthContract.UserInfoNotExistException e)
        {
            currentScreen = AuthContract.Screen.USER_INFO;
            router().toUserInfo();
        }
        catch(ErrorsContract.UnknownException e)
        {
            view().error();
        }
    }
}