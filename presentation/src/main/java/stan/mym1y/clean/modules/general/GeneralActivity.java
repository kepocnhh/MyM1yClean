package stan.mym1y.clean.modules.general;

import android.os.Build;
import android.view.View;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.auth.AuthFragment;
import stan.mym1y.clean.modules.main.MainFragment;
import stan.mym1y.clean.units.activities.UtilActivity;

public class GeneralActivity
    extends UtilActivity
{
    private GeneralContract.Presenter presenter;
    private final GeneralContract.View view = new GeneralContract.View()
    {
    };
    private final GeneralContract.Router router = new GeneralContract.Router()
    {
        public void toAuth()
        {
            log("to -> auth");
            replace(R.id.subscreen, AuthFragment.newInstanse(authBehaviour));
        }
        public void toMain()
        {
            log("to -> main");
            replace(R.id.subscreen, MainFragment.newInstanse(mainBehaviour));
        }
    };
    private final AuthContract.Behaviour authBehaviour = new AuthContract.Behaviour()
    {
        public void enter(UserPrivateData data)
        {
            presenter.enter(data);
        }
    };
    private final MainContract.Behaviour mainBehaviour = new MainContract.Behaviour()
    {
        public void logout()
        {
            presenter.logout();
        }
    };

    protected int getContentView()
    {
        return R.layout.general_screen;
    }
    protected void initViews()
    {
    }
    protected void init()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setStatusBarColor(getResources().getColor(R.color.white));
        presenter = new GeneralPresenter(view, new GeneralModel(App.component().dataLocal().transactionsAccess().transactions(), App.component().settings()), router);
        presenter.checkAuth();
    }
}