package stan.mym1y.clean.modules.auth.login;

import android.animation.Animator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
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

    private EditText login;
    private EditText password;
    private View waiter;
    private View login_container;
    private View password_container;
    private View signin;
    private View to_signup;

    private final Interpolator inInterpolator = new DecelerateInterpolator();

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
            case R.id.to_signup:
                animateOut(new Runnable()
                {
                    public void run()
                    {
                        behaviour.toSignup();
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
        login = findView(R.id.login);
        password = findView(R.id.password);
        waiter = findView(R.id.waiter);
        login_container = findView(R.id.login_container);
        password_container = findView(R.id.password_container);
        signin = findView(R.id.signin);
        to_signup = findView(R.id.to_signup);
        setClickListener(findView(R.id.signin), to_signup);
    }
    protected void init()
    {
        presenter = new LoginPresenter(view, new LoginModel(App.component().dataRemote().authApi()));
        hideWaiter();
        login_container.setVisibility(View.INVISIBLE);
        password_container.setVisibility(View.INVISIBLE);
        signin.setVisibility(View.INVISIBLE);
        to_signup.setVisibility(View.INVISIBLE);
        mainView().post(new Runnable()
        {
            public void run()
            {
                animateIn();
                mainView().requestFocus();
            }
        });
    }
    private void animateIn()
    {
        animateIn(login_container, 0);
        animateIn(password_container, 150);
        animateIn(signin, 300);
        animateIn(to_signup, 450);
    }
    private void animateIn(final View v, long delay)
    {
        float x = v.getX();
        v.setX(-v.getWidth());
        v.setAlpha(0);
        v.setVisibility(View.VISIBLE);
        v.animate()
         .setListener(new Animator.AnimatorListener()
         {
             public void onAnimationStart(Animator animation)
             {
                 v.setVisibility(View.VISIBLE);
             }
             public void onAnimationEnd(Animator animation)
             {
             }
             public void onAnimationCancel(Animator animation)
             {
             }
             public void onAnimationRepeat(Animator animation)
             {
             }
         })
         .setInterpolator(inInterpolator)
         .setStartDelay(delay)
         .x(x)
         .alpha(1)
         .setDuration(300);
    }
    private void animateOut(final View v, long delay, final Runnable runnable)
    {
        v.setAlpha(1);
        v.animate()
         .setListener(new Animator.AnimatorListener()
         {
             public void onAnimationStart(Animator animation)
             {
             }
             public void onAnimationEnd(Animator animation)
             {
                 v.setVisibility(View.GONE);
                 runnable.run();
             }
             public void onAnimationCancel(Animator animation)
             {
             }
             public void onAnimationRepeat(Animator animation)
             {
             }
         })
         .setStartDelay(delay)
         .x(-v.getWidth())
         .alpha(0)
         .setDuration(300);
    }
    private void animateOut(final View v, long delay)
    {
        v.setAlpha(1);
        v.animate()
         .setListener(new Animator.AnimatorListener()
         {
             public void onAnimationStart(Animator animation)
             {
             }
             public void onAnimationEnd(Animator animation)
             {
                 v.setVisibility(View.GONE);
             }
             public void onAnimationCancel(Animator animation)
             {
             }
             public void onAnimationRepeat(Animator animation)
             {
             }
         })
         .setStartDelay(delay)
         .x(-v.getWidth())
         .alpha(0)
         .setDuration(300);
    }
    private void animateOut(Runnable runnable)
    {
        animateOut(to_signup, 0);
        animateOut(signin, 150);
        animateOut(password_container, 300);
        animateOut(login_container, 400, runnable);
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