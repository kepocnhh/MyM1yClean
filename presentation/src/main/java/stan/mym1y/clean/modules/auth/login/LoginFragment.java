package stan.mym1y.clean.modules.auth.login;

import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
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
        public void error(AuthContract.NetworkErrorException exception)
        {
            showToast("NetworkErrorException");
        }
        @Override
        public void error(AuthContract.UnauthorizedException exception)
        {
            showToast("UnauthorizedException");
        }
        @Override
        public void error(AuthContract.InvalidDataException exception)
        {
            showToast("InvalidDataException");
        }
        @Override
        public void error(AuthContract.ServerErrorException exception)
        {
            showToast("ServerErrorException");
        }
        @Override
        public void error(AuthContract.UnknownErrorException exception)
        {
            showToast("UnknownErrorException");
        }
        @Override
        public void error(LoginContract.ValidateDataException exception)
        {
            showToast("ValidateDataException");
        }
        @Override
        public void success(String token)
        {
            behaviour.login(token);
        }
    };

    private LoginContract.Behaviour behaviour;

    private EditText login;
    private EditText password;

    @Override
    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.confirm:
                getPresenter().login(login.getText().toString(), password.getText().toString());
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
        setClickListener(findView(R.id.confirm));
    }

    @Override
    protected void init()
    {
        setPresenter(new LoginPresenter(view, new LoginModel(App.getAppComponent().getConnection())));
    }
}