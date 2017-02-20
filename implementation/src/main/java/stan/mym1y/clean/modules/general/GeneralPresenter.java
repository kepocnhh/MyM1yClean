package stan.mym1y.clean.modules.general;

import stan.mym1y.clean.contracts.GeneralContract;
import stan.mym1y.clean.units.mvp.ModelRouterPresenter;

class GeneralPresenter
    extends ModelRouterPresenter<GeneralContract.View, GeneralContract.Model, GeneralContract.Router>
    implements GeneralContract.Presenter
{
    public GeneralPresenter(GeneralContract.View v, GeneralContract.Model m, GeneralContract.Router r)
    {
        super(v, m, r);
    }

    @Override
    public void checkAuth()
    {
        onNewThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    getRouter().toMain(getModel().getToken());
                }
                catch(GeneralContract.UserNotAuthorizedException e)
                {
                    getRouter().toAuth();
                }
            }
        });
    }

    @Override
    public void enter(String token)
    {
        getRouter().toMain(token);
    }
}