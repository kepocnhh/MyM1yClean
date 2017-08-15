package stan.mym1y.clean.modules.settings;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.work.SettingsContract;
import stan.mym1y.clean.cores.users.UserInfo;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class SettingsFragment
    extends UtilFragment
{
    private SettingsContract.Presenter presenter;
    private final SettingsContract.View view = new SettingsContract.View()
    {
        public void update(final UserInfo info)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(info.avatar() == null)
                    {
                        emptyAvatar();
                    }
                    else
                    {
                        updateAvatar(info.avatar());
                    }
                }
            });
        }
        private void updateAvatar(String name)
        {
            empty_avatar.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.INVISIBLE);
        }
        private void emptyAvatar()
        {
            empty_avatar.setVisibility(View.INVISIBLE);
            avatar.setVisibility(View.VISIBLE);
        }
    };

    private ImageView empty_avatar;
    private ImageView avatar;
    private TextView change_avatar;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.change_avatar:
                log("try change avatar");
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.settings_screen;
    }
    protected void initViews(View v)
    {
        empty_avatar = findView(R.id.empty_avatar);
        avatar = findView(R.id.avatar);
        change_avatar = findView(R.id.change_avatar);
        setClickListener(change_avatar);
    }
    protected void init()
    {
        empty_avatar.setVisibility(View.INVISIBLE);
        avatar.setVisibility(View.INVISIBLE);
        presenter = new SettingsPresenter(view, new SettingsModel(App.component().settings()));
        presenter.update();
    }
}