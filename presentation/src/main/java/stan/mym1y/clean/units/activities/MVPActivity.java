package stan.mym1y.clean.units.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public abstract class MVPActivity<PRESENTER>
    extends Activity
{
    private PRESENTER presenter;
    private View.OnClickListener clickListener;
    private String tag;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(getContentView());
        tag = "["+getClass().getName().replace(getClass().getPackage().getName()+".", "")+"]";
        clickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickView(view.getId());
            }
        };
        initViews();
        init();
    }

    final protected void setClickListener(View... views)
    {
        for(View v : views)
        {
            if(v != null)
            {
                v.setOnClickListener(clickListener);
            }
        }
    }
    protected <VIEW extends View> VIEW findView(int id)
    {
        return (VIEW)findViewById(id);
    }
    protected void onClickView(int id)
    {
    }
    protected void log(String message)
    {
        Log.e(tag, message);
    }
    protected void setPresenter(PRESENTER p)
    {
        presenter = p;
    }
    protected PRESENTER getPresenter()
    {
        return presenter;
    }

    abstract protected int getContentView();
    abstract protected void initViews();
    abstract protected void init();
}