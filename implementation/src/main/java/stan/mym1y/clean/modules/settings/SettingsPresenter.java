package stan.mym1y.clean.modules.settings;

import stan.mym1y.clean.contracts.work.SettingsContract;
import stan.mym1y.clean.units.mvp.ModelPresenter;

public class SettingsPresenter
    extends ModelPresenter<SettingsContract.View, SettingsContract.Model>
    implements SettingsContract.Presenter
{
    SettingsPresenter(SettingsContract.View v, SettingsContract.Model m)
    {
        super(v, m);
    }

    public void update()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                view().update(model().getUserInfo());
            }
        });
    }
}