package stan.mym1y.clean.modules.auth;

import android.view.View;

import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.auth.login.LoginFragment;
import stan.mym1y.clean.modules.auth.registration.RegistrationFragment;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class AuthFragment
    extends UtilFragment
{
    static public UtilFragment newInstanse(AuthContract.Behaviour b)
    {
        AuthFragment fragment = new AuthFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private AuthContract.Presenter presenter;
    private final AuthContract.View view = new AuthContract.View()
    {
    };
    private final AuthContract.Router router = new AuthContract.Router()
    {
        public void toLogin()
        {
            log("to -> login");
            replace(R.id.auth_subscreen, LoginFragment.newInstanse(loginBehaviour));
        }
        public void toRegistration()
        {
            log("to -> registration");
            replace(R.id.auth_subscreen, RegistrationFragment.newInstanse(registrationBehaviour));
        }
    };
    private final LoginContract.Behaviour loginBehaviour = new LoginContract.Behaviour()
    {
        public void login(UserPrivateData data)
        {
            log("login:"
                    + "\n\t" + "userId " + data.getUserId()
                    + "\n\t" + "token " + data.getUserToken());
            behaviour.enter(data);
        }
        public void toSignup()
        {
            presenter.toRegistration();
        }
    };
    private final RegistrationContract.Behaviour registrationBehaviour = new RegistrationContract.Behaviour()
    {
        public void registration(UserPrivateData data)
        {
            log("registration:"
                    + "\n\t" + "userId " + data.getUserId()
                    + "\n\t" + "token " + data.getUserToken());
            behaviour.enter(data);
        }
        public void toSignin()
        {
            presenter.toLogin();
        }
    };

    private AuthContract.Behaviour behaviour;

    protected int getContentView()
    {
        return R.layout.auth_screen;
    }
    protected void initViews(View v)
    {
    }
    protected void init()
    {
        presenter = new AuthPresenter(view, router);
        presenter.toLogin();
    }
}