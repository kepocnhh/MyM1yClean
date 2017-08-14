package stan.mym1y.clean.modules.userinfo;

import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.work.UserInfoCreateContract;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.units.mvp.ModelPresenter;

class UserInfoCreatePresenter
    extends ModelPresenter<UserInfoCreateContract.View, UserInfoCreateContract.Model>
    implements UserInfoCreateContract.Presenter
{
    UserInfoCreatePresenter(UserInfoCreateContract.View v, UserInfoCreateContract.Model m)
    {
        super(v, m);
    }

    public void save(final UserInfo info)
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().checkData(info);
                    model().save(info);
                    view().success(info);
                }
                catch(UserInfoCreateContract.ValidateDataException e)
                {
                    view().error(e);
                }
                catch(ErrorsContract.UnknownException e)
                {
                    view().error();
                }
                catch(ErrorsContract.NetworkException e)
                {
                    view().error(e);
                }
                catch(ErrorsContract.UnauthorizedException e)
                {
                    view().error(e);
                }
            }
        });
    }
}