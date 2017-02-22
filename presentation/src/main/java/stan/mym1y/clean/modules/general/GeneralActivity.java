package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.modules.auth.AuthFragment;
import stan.mym1y.clean.modules.main.MainFragment;
import stan.mym1y.clean.units.activities.MVPActivity;

public class GeneralActivity
    extends MVPActivity<GeneralContract.Presenter>
{
    private final GeneralContract.View view = new GeneralContract.View()
    {
    };
    private final GeneralContract.Router router = new GeneralContract.Router()
    {
        @Override
        public void toAuth()
        {
            getFragmentManager().beginTransaction().replace(R.id.subscreen, AuthFragment.newInstanse(authBehaviour)).commit();
        }
        @Override
        public void toMain(UserPrivateData data)
        {
            getFragmentManager().beginTransaction().replace(R.id.subscreen, MainFragment.newInstanse(mainBehaviour)).commit();
        }
    };
    private final AuthContract.Behaviour authBehaviour = new AuthContract.Behaviour()
    {
        @Override
        public void enter(UserPrivateData data)
        {
            getPresenter().enter(data);
        }
    };
    private final MainContract.Behaviour mainBehaviour = new MainContract.Behaviour()
    {
        @Override
        public void logout()
        {
            getPresenter().logout();
        }
    };

    @Override
    protected int getContentView()
    {
        return R.layout.general_screen;
    }
    @Override
    protected void initViews()
    {
    }
    @Override
    protected void init()
    {
        setPresenter(new GeneralPresenter(view, new GeneralModel(App.getAppComponent().getDataAccess().getTransactions(), App.getAppComponent().getSettings()), router));
        getPresenter().checkAuth();
    }
}