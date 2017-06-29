package stan.mym1y.clean.modules.auth.registration;

import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.RegistrationContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class RegistrationFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(RegistrationContract.Behaviour b)
    {
        RegistrationFragment fragment = new RegistrationFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private RegistrationContract.Presenter presenter;
    private final RegistrationContract.View view = new RegistrationContract.View()
    {
        public void error(ErrorsContract.NetworkException e)
        {
            hideWaiter();
            toast("NetworkException " + e.getMessage());
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
            hideWaiter();
            toast("UnauthorizedException");
        }
        public void error(RegistrationContract.ValidateDataException e)
        {
            hideWaiter();
            switch(e.error)
            {
                case EMPTY_LOGIN:
                    toast(R.string.empty_login_error_message);
                    break;
                case EMPTY_PASSWORD:
                    toast(R.string.empty_password_error_message);
                    break;
                case LOGIN_VALID:
                    toast(R.string.login_valid_error_message);
                    break;
                case PASSWORD_LENGTH:
                    toast(R.string.password_length_error_message);
                    break;
            }
        }
        public void error()
        {
            hideWaiter();
            toast("UnknownErrorException");
        }
        public void success(UserPrivateData data)
        {
            behaviour.registration(data);
//            hideWaiter();
        }
    };

    private RegistrationContract.Behaviour behaviour;

    private EditText login;
    private EditText password;
    private View waiter;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.signup:
                showWaiter();
                hideKeyBoard();
                onNewThread(new Runnable()
                {
                    public void run()
                    {
                        presenter.registration(login.getText().toString(), password.getText().toString());
                    }
                }, 300);
                break;
            case R.id.to_signin:
                behaviour.toSignin();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.registration_screen;
    }
    protected void initViews(View v)
    {
        login = findView(R.id.login);
        password = findView(R.id.password);
        waiter = findView(R.id.waiter);
        setClickListener(findView(R.id.signup), findView(R.id.to_signin));
    }
    protected void init()
    {
        presenter = new RegistrationPresenter(view, new RegistrationModel(App.component().dataRemote().authApi()));
        hideWaiter();
    }

    private void showWaiter()
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                waiter.setVisibility(View.VISIBLE);
            }
        });
    }
    private void hideWaiter()
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                waiter.setVisibility(View.GONE);
            }
        });
    }
}