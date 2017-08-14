package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.contracts.StartContract;
import stan.mym1y.clean.contracts.work.WorkContract;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.auth.AuthFragment;
import stan.mym1y.clean.modules.start.StartFragment;
import stan.mym1y.clean.modules.work.WorkFragment;
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
        public void toStart()
        {
            log("to -> start");
            replace(R.id.subscreen, StartFragment.newInstance(startBehaviour));
        }
        public void toAuth()
        {
            log("to -> auth");
            replace(R.id.subscreen, AuthFragment.newInstance(authBehaviour));
        }
        public void toWork()
        {
            log("to -> work");
            replace(R.id.subscreen, WorkFragment.newInstance(workBehaviour));
        }
    };
    private final StartContract.Behaviour startBehaviour = new StartContract.Behaviour()
    {
        public void sync()
        {
            presenter.checkAuth();
        }
    };
    private final AuthContract.Behaviour authBehaviour = new AuthContract.Behaviour()
    {
        public void enter(UserPrivateData data)
        {
            presenter.enter(data);
        }
    };
    private final WorkContract.Behaviour workBehaviour = new WorkContract.Behaviour()
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
        setSystemUiVisibilityLight(true);
        setStatusBarColor(getResources().getColor(R.color.white));
        presenter = new GeneralPresenter(view, new GeneralModel(App.component().dataLocal().cashAccountsAccess().cashAccounts(),
                App.component().dataLocal().transactionsAccess().transactions(),
                App.component().settings(),
                App.component().dataRemote().globalDataApi()), router);
        presenter.start();
    }
}