package stan.mym1y.clean.modules.auth;

import android.view.View;

import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.contracts.auth.LoginWithProviderContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.auth.login.LoginFragment;
import stan.mym1y.clean.modules.auth.provider.LoginWithProviderFragment;
import stan.mym1y.clean.modules.auth.registration.RegistrationFragment;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class AuthFragment
    extends UtilFragment
{
    static public UtilFragment newInstance(AuthContract.Behaviour b)
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
            replace(R.id.auth_subscreen, LoginFragment.newInstance(loginBehaviour));
        }
        public void toLogin(Providers.Type type)
        {
            log("to -> login with type: " + type.value);
            replace(R.id.auth_subscreen, LoginWithProviderFragment.newInstance(type, loginWithProviderBehaviour));
        }
        public void toRegistration()
        {
            log("to -> registration");
            replace(R.id.auth_subscreen, RegistrationFragment.newInstance(registrationBehaviour));
        }
    };
    private final LoginContract.Behaviour loginBehaviour = new LoginContract.Behaviour()
    {
        public void login(UserPrivateData data)
        {
            log("login:"
                    + "\n\t" + "userId " + data.userId()
                    + "\n\t" + "token " + data.userToken());
            behaviour.enter(data);
        }
        public void toLogin(Providers.Type type)
        {
            presenter.toLogin(type);
        }
        public void toSignup()
        {
            presenter.toRegistration();
        }
    };
    private final LoginWithProviderContract.Behaviour loginWithProviderBehaviour = new LoginWithProviderContract.Behaviour()
    {
        public void login(UserPrivateData data)
        {
            log("login:"
                    + "\n\t" + "userId " + data.userId()
                    + "\n\t" + "token " + data.userToken());
            behaviour.enter(data);
        }
        public void exit()
        {
            presenter.toLogin();
        }
    };
    private final RegistrationContract.Behaviour registrationBehaviour = new RegistrationContract.Behaviour()
    {
        public void registration(UserPrivateData data)
        {
            log("registration:"
                    + "\n\t" + "userId " + data.userId()
                    + "\n\t" + "token " + data.userToken());
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
        setStatusBarColor(getActivity().getResources().getColor(R.color.white));
        setSystemUiVisibilityLight(true);
        setNavigationBarColor(getActivity().getResources().getColor(R.color.black));
        presenter = new AuthPresenter(view, router);
        presenter.toLogin();
    }
}