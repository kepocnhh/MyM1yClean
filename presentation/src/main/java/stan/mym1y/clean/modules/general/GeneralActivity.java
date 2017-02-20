package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.contracts.auth.AuthContract;
import stan.mym1y.clean.modules.auth.AuthFragment;
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
        public void toMain(String token)
        {
            log("toMain " + token);
        }
    };
    private final AuthContract.Behaviour authBehaviour = new AuthContract.Behaviour()
    {
        @Override
        public void login(String token)
        {
            getPresenter().enter(token);
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
        setPresenter(new GeneralPresenter(view, new GeneralModel(App.getAppComponent().getSettings()), router));
        getPresenter().checkAuth();
    }
}