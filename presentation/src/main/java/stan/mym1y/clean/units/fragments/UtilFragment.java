package stan.mym1y.clean.units.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import stan.mym1y.clean.utils.FontChangeCrawler;

public abstract class UtilFragment
        extends Fragment
{
    static private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private View.OnClickListener clickListener;
    private View mainView;
    private String tag;
    private InputMethodManager inputMethodManager;
    private float density;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        if(mainView == null)
        {
            tag = "[" + getClass().getName().replace(getClass().getPackage().getName() + ".", "") + "]";
            density = getActivity().getResources().getDisplayMetrics().density;
            mainView = inflater.inflate(getContentView(), container, false);
            new FontChangeCrawler(getActivity().getAssets(), "fonts/main.otf").replaceFonts(mainView);
            clickListener = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    onClickView(view.getId());
                }
            };
            inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            initViews(mainView);
            init();
        }
        return mainView;
    }
    final protected View mainView()
    {
        return mainView;
    }

    final protected void setClickListener(View view1, View... views)
    {
        if(view1 != null)
        {
            view1.setOnClickListener(clickListener);
        }
        for(View v : views)
        {
            if(v != null)
            {
                v.setOnClickListener(clickListener);
            }
        }
    }
    final protected <VIEW extends View> VIEW findView(int id)
    {
        return (VIEW)mainView.findViewById(id);
    }
    protected void onClickView(int id)
    {
    }

    final protected void log(String message)
    {
        Log.e(tag, message);
    }
    final protected void onNewThread(Runnable r)
    {
        new Thread(r).start();
    }
    final protected void onNewThread(Runnable r, final long ms)
    {
        new Thread(r)
        {
            public void run()
            {
                try
                {
                    sleep(ms);
                }
                catch(InterruptedException e)
                {
                }
                super.run();
            }
        }.start();
    }
    final protected void runOnUiThread(Runnable r)
    {
        uiHandler.post(r);
    }
    final protected void runAfterResume(final Runnable r)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                while(!isResumed())
                {
                }
                runOnUiThread(r);
            }
        }).start();
    }
    final protected void toast(final int messageId)
    {
        uiHandler.post(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getActivity(), messageId, Toast.LENGTH_SHORT).show();
            }
        });
    }
    final protected void toast(final String message)
    {
        uiHandler.post(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    final protected void hideKeyBoard()
    {
        inputMethodManager.hideSoftInputFromWindow(mainView.getWindowToken(), 0);
    }
    final protected boolean hasNavigationBar()
    {
        return !ViewConfiguration.get(getActivity()).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
    }
    final protected void setSystemUiVisibilityLight(final boolean light)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(light ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0);
        }
    }
    final protected void setStatusBarColor(final int color)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    final protected void setNavigationBarColor(final int color)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getActivity().getWindow();
            window.setNavigationBarColor(color);
        }
    }

    final protected void replace(final int id, final Fragment fragment)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                hideKeyBoard();
                getFragmentManager().beginTransaction()
                                    .replace(id, fragment)
                                    .commit();
            }
        });
    }
    final protected void clear(final int id)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                hideKeyBoard();
                getFragmentManager().beginTransaction()
                                    .remove(getFragmentManager().findFragmentById(id))
                                    .commit();
            }
        });
    }

    final protected int px(float dp)
    {
        if(dp < 0)
        {
            return 0;
        }
        return (int)Math.ceil(density * dp);
    }

    abstract protected int getContentView();
    abstract protected void initViews(View v);
    abstract protected void init();
}