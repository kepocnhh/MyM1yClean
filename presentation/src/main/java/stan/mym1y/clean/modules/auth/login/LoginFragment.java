package stan.mym1y.clean.modules.auth.login;

import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class LoginFragment
        extends UtilFragment
{
    static public UtilFragment newInstanse(LoginContract.Behaviour b)
    {
        LoginFragment fragment = new LoginFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private LoginContract.Presenter presenter;
    private final LoginContract.View view = new LoginContract.View()
    {
        public void error(ErrorsContract.NetworkException e)
        {
            hideWaiter();
            showToast("NetworkException");
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
            hideWaiter();
            showToast("UnauthorizedException");
        }
        public void error()
        {
            showToast("UnknownErrorException");
        }
        public void error(LoginContract.ValidateDataException e)
        {
            hideWaiter();
            switch(e.error)
            {
                case EMPTY_LOGIN:
                    showToast(R.string.empty_login_error_message);
                    break;
                case EMPTY_PASSWORD:
                    showToast(R.string.empty_password_error_message);
                    break;
                case LOGIN_VALID:
                    showToast(R.string.login_valid_error_message);
                    break;
                case PASSWORD_LENGTH:
                    showToast(R.string.password_length_error_message);
                    break;
            }
        }
        public void success(UserPrivateData data)
        {
            behaviour.login(data);
//            hideWaiter();
        }
    };

    private LoginContract.Behaviour behaviour;

    private EditText login;
    private EditText password;
    private View waiter;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.signin:
                showWaiter();
                hideKeyBoard();
                runOnNewThread(new Runnable()
                {
                    public void run()
                    {
                        presenter.login(login.getText().toString(), password.getText().toString());
                    }
                }, 300);
                break;
            case R.id.to_signup:
                behaviour.toSignup();
                break;
        }
    }

    protected int getContentView()
    {
        return R.layout.login_screen;
    }
    protected void initViews(View v)
    {
        login = findView(R.id.login);
        password = findView(R.id.password);
        waiter = findView(R.id.waiter);
        setClickListener(findView(R.id.signin), findView(R.id.to_signup));
    }
    protected void init()
    {
        presenter = new LoginPresenter(view, new LoginModel(App.component().dataRemote().authApi()));
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