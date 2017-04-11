package stan.mym1y.clean.units.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import stan.mym1y.clean.utils.FontChangeCrawler;

public abstract class UtilDialog
        extends DialogFragment
{
    private View.OnClickListener clickListener;
    private View mainView;
    private String tag;
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        if(mainView == null)
        {
            tag = "[" + getClass().getName().replace(getClass().getPackage().getName() + ".", "") + "]";
            mainView = inflater.inflate(getContentView(), container, false);
            new FontChangeCrawler(getActivity().getAssets(), "fonts/main.otf").replaceFonts(mainView);
            clickListener = new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    onClickView(view.getId());
                }
            };
            initViews(mainView);
            init();
        }
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
    final protected void runOnUiThread(Runnable r)
    {
        uiHandler.post(r);
    }
    final protected void showToast(final String message)
    {
        uiHandler.post(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    abstract protected int getContentView();
    abstract protected void initViews(View v);
    abstract protected void init();
}