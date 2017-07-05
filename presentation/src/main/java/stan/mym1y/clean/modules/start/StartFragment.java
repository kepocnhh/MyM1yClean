package stan.mym1y.clean.modules.start;

import android.view.View;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.StartContract;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class StartFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(StartContract.Behaviour b)
    {
        StartFragment fragment = new StartFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private StartContract.Presenter presenter;
    private final StartContract.View view = new StartContract.View()
    {
        public void complete()
        {
            behaviour.sync();
        }
        public void error(StartContract.CantContinueWithoutDataException e)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    progress.setVisibility(View.GONE);
                    error_container.setVisibility(View.VISIBLE);
                }
            });
            log("Cant Continue Without Data!");
        }
        public void error(StartContract.DataNeedUpdateException e)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    progress.setVisibility(View.GONE);
                    error_container.setVisibility(View.VISIBLE);
                }
            });
            log("Data Need Update!");
        }
        public void error()
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    progress.setVisibility(View.GONE);
                    error_container.setVisibility(View.VISIBLE);
                }
            });
            log("Unknown error!");
        }
    };
    private StartContract.Behaviour behaviour;

    private View progress;
    private View error_container;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.try_again:
                progress.setVisibility(View.VISIBLE);
                error_container.setVisibility(View.GONE);
                presenter.checkSync();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.start_screen;
    }
    protected void initViews(View v)
    {
        progress = findView(R.id.progress);
        error_container = findView(R.id.error_container);
        setClickListener(findView(R.id.try_again));
    }
    protected void init()
    {
        progress.setVisibility(View.VISIBLE);
        error_container.setVisibility(View.GONE);
        presenter = new StartPresenter(view, new StartModel(App.component().settings(),
                App.component().dataLocal().currenciesAccess().currencies(),
                App.component().dataRemote().globalDataApi()));
        presenter.checkSync();
    }
}