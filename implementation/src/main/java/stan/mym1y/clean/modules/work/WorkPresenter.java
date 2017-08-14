package stan.mym1y.clean.modules.work;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.WorkContract;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.units.mvp.ModelRouterPresenter;

class WorkPresenter
        extends ModelRouterPresenter<WorkContract.View, WorkContract.Model, WorkContract.Router>
        implements WorkContract.Presenter
{
    WorkPresenter(WorkContract.View v, WorkContract.Model m, WorkContract.Router r)
    {
        super(v, m, r);
    }

    public void start()
    {
        log("start...");
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().checkUserInfo();
                    router().toMain();
                }
                catch(WorkContract.UserInfoNotExistException e)
                {
                    log("User Info Not Exist");
                    router().toUserInfo();
                }
                catch(ErrorsContract.UnauthorizedException e)
                {
                    log("Unauthorized!");
                    view().error(e);
                }
                catch(ErrorsContract.UnknownException e)
                {
                    log("Unknown error!\n" + e.getMessage());
                    view().error();
                }
            }
        });
    }
    public void setUserInfo(final UserInfo info)
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                model().setUserInfo(info);
                router().toMain();
            }
        });
    }
}