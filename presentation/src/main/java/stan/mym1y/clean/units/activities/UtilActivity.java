package stan.mym1y.clean.units.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import stan.mym1y.clean.utils.FontChangeCrawler;

public abstract class UtilActivity
        extends Activity
{
    private View.OnClickListener clickListener;
    private String tag;

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(getContentView());
        tag = "["+getClass().getName().replace(getClass().getPackage().getName()+".", "")+"]";
        new FontChangeCrawler(getAssets(), "fonts/main.otf").replaceFonts(findViewById(android.R.id.content));
        clickListener = new View.OnClickListener()
        {
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
    protected <VIEW extends View> VIEW view(int id)
    {
        return (VIEW)findViewById(id);
    }
    protected void onClickView(int id)
    {
    }

    final protected void toast(final String message)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(UtilActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    final protected void replace(final int id, final Fragment fragment)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                getFragmentManager().beginTransaction().replace(id, fragment).commit();
            }
        });
    }
    protected void log(String message)
    {
        Log.e(tag, message);
//        Log.e(getClass().getName(), message);
    }
    final protected void setSystemUiVisibilityLight(final boolean light)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if(light)
                    {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    else
                    {
                        getWindow().getDecorView().dispatchSystemUiVisibilityChanged(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                }
            });
        }
    }
    final protected void setStatusBarColor(final int color)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Window window = getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(color);
                }
            });
        }
    }

    abstract protected int getContentView();
    abstract protected void initViews();
    abstract protected void init();
}