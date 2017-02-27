package stan.mym1y.clean.modules.auth.login;

import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.fragments.MVPFragment;

public class LoginFragment
        extends MVPFragment<LoginContract.Presenter>
{
    static public MVPFragment newInstanse(LoginContract.Behaviour b)
    {
        LoginFragment fragment = new LoginFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private final LoginContract.View view = new LoginContract.View()
    {
        @Override
        public void error(ErrorsContract.NetworkErrorException exception)
        {
            hideWaiter();
            showToast("NetworkErrorException");
        }
        @Override
        public void error(ErrorsContract.UnauthorizedException exception)
        {
            hideWaiter();
            showToast("UnauthorizedException");
        }
        @Override
        public void error(ErrorsContract.InvalidDataException exception)
        {
            showToast("InvalidDataException");
        }
        @Override
        public void error(ErrorsContract.ServerErrorException exception)
        {
            showToast("ServerErrorException");
        }
        @Override
        public void error(ErrorsContract.UnknownErrorException exception)
        {
            showToast("UnknownErrorException");
        }
        @Override
        public void error(LoginContract.ValidateDataException exception)
        {
            hideWaiter();
            showToast("ValidateDataException");
        }
        @Override
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

    @Override
    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.signin:
                showWaiter();
                hideKeyBoard();
                runOnNewThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getPresenter().login(login.getText().toString(), password.getText().toString());
                    }
                }, 300);
                break;
            case R.id.to_signup:
                behaviour.toSignup();
                break;
        }
    }

    @Override
    protected int getContentView()
    {
        return R.layout.login_screen;
    }

    @Override
    protected void initViews(View v)
    {
        login = findView(R.id.login);
        password = findView(R.id.password);
        waiter = findView(R.id.waiter);
        setClickListener(findView(R.id.signin), findView(R.id.to_signup));
    }

    @Override
    protected void init()
    {
        setPresenter(new LoginPresenter(view, new LoginModel(App.getAppComponent().getConnection(), App.getAppComponent().getJsonConverter())));
        hideWaiter();
    }

    private void showWaiter()
    {
        runOnUiThread(new Runnable()
        {
            @Override
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
            @Override
            public void run()
            {
                waiter.setVisibility(View.GONE);
            }
        });
    }
}