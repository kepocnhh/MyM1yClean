package stan.mym1y.clean.modules.userinfo;

import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.UserInfoCreateContract;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.modules.users.UserInfoData;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class UserInfoCreateFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(UserInfoCreateContract.Behaviour b)
    {
        UserInfoCreateFragment fragment = new UserInfoCreateFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private UserInfoCreateContract.Presenter presenter;
    private final UserInfoCreateContract.View view = new UserInfoCreateContract.View()
    {
        public void error(ErrorsContract.NetworkException e)
        {
            hideWaiter();
            toast("Network error!");
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
            toast("Unauthorized!");
            behaviour.unauthorized();
        }
        public void error(UserInfoCreateContract.ValidateDataException e)
        {
            hideWaiter();
            switch(e.error())
            {
                case EMPTY_NAME:
                    toast(R.string.empty_user_info_name_error_message);
                    break;
            }
        }
        public void error()
        {
            hideWaiter();
            toast("Unknown error!");
        }
        public void success(UserInfo info)
        {
            behaviour.success(info);
        }
    };
    private UserInfoCreateContract.Behaviour behaviour;

    private View waiter;
    private EditText name;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.save:
                showWaiter();
                hideKeyBoard();
                onNewThread(new Runnable()
                {
                    public void run()
                    {
                        presenter.save(collectUserInfo());
                    }
                }, 300);
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.user_info_create_screen;
    }
    protected void initViews(View v)
    {
        waiter = findView(R.id.waiter);
        name = findView(R.id.name);
        setClickListener(findView(R.id.save));
    }
    protected void init()
    {
        presenter = new UserInfoCreatePresenter(view, new UserInfoCreateModel(App.component().settings(), App.component().dataRemote().privateDataApi()));
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

    private UserInfo collectUserInfo()
    {
        return UserInfoData.create(name.getText().toString(), null, null, -1);
    }
}