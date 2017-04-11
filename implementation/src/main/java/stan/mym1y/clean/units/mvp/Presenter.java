package stan.mym1y.clean.units.mvp;

import android.util.Log;

public abstract class Presenter<VIEW>
{
    private final String tag;
    private final VIEW view;

    public Presenter(VIEW v)
    {
        view = v;
        tag = "["+getClass().getName().replace(getClass().getPackage().getName()+".", "")+"]";
    }

    final protected VIEW view()
    {
        return view;
    }

    final protected void onNewThread(Runnable runnable)
    {
        new Thread(runnable).start();
    }
    final protected void together(Runnable... runnables)
    {
        for(Runnable runnable : runnables)
        {
            new Thread(runnable).start();
        }
    }

    final protected void log(String message)
    {
        Log.e(tag, message);
    }
}