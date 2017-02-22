package stan.mym1y.clean.modules.auth;

import android.view.View;

import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.auth.login.LoginFragment;
import stan.mym1y.clean.modules.auth.registration.RegistrationFragment;
import stan.mym1y.clean.units.fragments.MVPFragment;

public class AuthFragment
    extends MVPFragment<AuthContract.Presenter>
{
    static public MVPFragment newInstanse(AuthContract.Behaviour b)
    {
        AuthFragment fragment = new AuthFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private final AuthContract.View view = new AuthContract.View()
    {
    };
    private final AuthContract.Router router = new AuthContract.Router()
    {
        @Override
        public void toLogin()
        {
            getFragmentManager().beginTransaction().replace(R.id.auth_subscreen, LoginFragment.newInstanse(loginBehaviour)).commit();
        }
        @Override
        public void toRegistration()
        {
            getFragmentManager().beginTransaction().replace(R.id.auth_subscreen, RegistrationFragment.newInstanse(registrationBehaviour)).commit();
        }
    };
    private final LoginContract.Behaviour loginBehaviour = new LoginContract.Behaviour()
    {
        @Override
        public void login(UserPrivateData data)
        {
            log("login: userId " + data.getUserId() + " token " + data.getUserToken());
            behaviour.enter(data);
        }
        @Override
        public void toSignup()
        {
            getPresenter().toRegistration();
        }
    };
    private final RegistrationContract.Behaviour registrationBehaviour = new RegistrationContract.Behaviour()
    {
        @Override
        public void registration(UserPrivateData data)
        {
            log("registration: userId " + data.getUserId() + " token " + data.getUserToken());
            behaviour.enter(data);
        }
        @Override
        public void toSignin()
        {
            getPresenter().toLogin();
        }
    };

    private AuthContract.Behaviour behaviour;

    @Override
    protected int getContentView()
    {
        return R.layout.auth_screen;
    }
    @Override
    protected void initViews(View v)
    {
    }
    @Override
    protected void init()
    {
        setPresenter(new AuthPresenter(view, router));
        getPresenter().toLogin();
    }
}