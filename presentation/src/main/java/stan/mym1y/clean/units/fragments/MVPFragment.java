package stan.mym1y.clean.units.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public abstract class MVPFragment<PRESENTER>
        extends Fragment
{
    private PRESENTER presenter;
    private View.OnClickListener clickListener;
    private View mainView;
    private String tag;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private InputMethodManager inputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(mainView == null)
        {
            tag = "["+getClass().getName().replace(getClass().getPackage().getName()+".", "")+"]";
            mainView = inflater.inflate(getContentView(), container, false);
            clickListener = new View.OnClickListener()
            {
                @Override
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
    final protected void runOnNewThread(Runnable r)
    {
        new Thread(r).start();
    }
    final protected void runOnNewThread(Runnable r, final long ms)
    {
        new Thread(r)
        {
            @Override
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
    final protected void showToast(final String message)
    {
        log(message);
        uiHandler.post(new Runnable()
        {
            @Override
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
    protected void setPresenter(PRESENTER p)
    {
        presenter = p;
    }
    protected PRESENTER getPresenter()
    {
        return presenter;
    }

    abstract protected int getContentView();
    abstract protected void initViews(View v);
    abstract protected void init();
}