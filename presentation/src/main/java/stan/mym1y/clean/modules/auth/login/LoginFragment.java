package stan.mym1y.clean.modules.auth.login;

import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.auth.LoginContract;
import stan.mym1y.clean.cores.auth.Providers;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.units.fragments.UtilFragment;
import stan.mym1y.clean.utils.ValueAnimator;

public class LoginFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(LoginContract.Behaviour b)
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
            toast("NetworkException");
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
            hideWaiter();
            toast("UnauthorizedException");
        }
        public void error(LoginContract.ValidateDataException e)
        {
            hideWaiter();
            switch(e.error())
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
            behaviour.login(data);
//            hideWaiter();
        }
    };

    private LoginContract.Behaviour behaviour;

    private View background;
    private EditText login;
    private EditText password;
    private View waiter;
    private View login_container;
    private View password_container;
    private View signin;
    private View auth_container;
    private View to_signup;

    private ValueAnimator.Animator animator;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.signin:
                showWaiter();
                hideKeyBoard();
                onNewThread(new Runnable()
                {
                    public void run()
                    {
                        presenter.login(login.getText().toString(), password.getText().toString());
                    }
                }, 300);
                break;
            case R.id.auth_google:
                log("try auth with google account");
                behaviour.toLogin(Providers.Type.GOOGLE);
                break;
            case R.id.to_signup:
                animate(false, new ValueAnimator.AnimationListener()
                {
                    public void begin()
                    {
                    }
                    public void end()
                    {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                login_container.setVisibility(View.INVISIBLE);
                                password_container.setVisibility(View.INVISIBLE);
                                signin.setVisibility(View.INVISIBLE);
                                auth_container.setVisibility(View.INVISIBLE);
                                to_signup.setVisibility(View.INVISIBLE);
                            }
                        });
                        behaviour.toSignup();
                    }
                    public void cancel()
                    {
                    }
                });
//                behaviour.toSignup();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.login_screen;
    }
    protected void initViews(View v)
    {
        background = findView(R.id.background);
        login = findView(R.id.login);
        password = findView(R.id.password);
        waiter = findView(R.id.waiter);
        login_container = findView(R.id.login_container);
        password_container = findView(R.id.password_container);
        signin = findView(R.id.signin);
        auth_container = findView(R.id.auth_container);
        to_signup = findView(R.id.to_signup);
        setClickListener(findView(R.id.signin), findView(R.id.auth_google), to_signup);
    }
    protected void init()
    {
        presenter = new LoginPresenter(view, new LoginModel(App.component().dataRemote().authApi()));
        hideWaiter();
        login_container.setVisibility(View.INVISIBLE);
        password_container.setVisibility(View.INVISIBLE);
        signin.setVisibility(View.INVISIBLE);
        auth_container.setVisibility(View.INVISIBLE);
        to_signup.setVisibility(View.INVISIBLE);
        mainView().post(new Runnable()
        {
            public void run()
            {
                animate(true, new ValueAnimator.AnimationListener()
                {
                    public void begin()
                    {
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                background.setAlpha(0);
                                login_container.setVisibility(View.VISIBLE);
                                password_container.setVisibility(View.VISIBLE);
                                signin.setVisibility(View.VISIBLE);
                                auth_container.setVisibility(View.VISIBLE);
                                to_signup.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    public void end()
                    {
                    }
                    public void cancel()
                    {
                    }
                });
                mainView().requestFocus();
            }
        });
    }
    private void animate(final boolean in, ValueAnimator.AnimationListener listener)
    {
        if(animator != null)
        {
            animator.cancel();
        }
        animator = ValueAnimator.create(450, in ? 0 : 1, in ? 1 : 0, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        background.setAlpha(value);
                        login_container.setX(getX(value, 1));
                        password_container.setX(getX(value, 0.85f));
                        signin.setX(getX(value, 0.7f));
                        auth_container.setX(getX(value, 0.55f));
                        to_signup.setX(getX(value, 0.3f));
                    }
                });
            }
        });
        animator.setAnimationListener(listener);
        animator.animate();
    }
    private float getX(float value, float delay)
    {
        return mainView().getWidth()*(value > delay ? 1 : value/delay) - mainView().getWidth();
//        return mainView().getWidth()*(value > delay ? 1 : value + (1-delay)) - mainView().getWidth();
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