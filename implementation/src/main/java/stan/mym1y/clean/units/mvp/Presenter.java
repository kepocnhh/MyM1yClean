package stan.mym1y.clean.units.mvp;

import android.util.Log;

public abstract class Presenter<V>
{
    private final String tag;
    private final V view;

    public Presenter(V v)
    {
        view = v;
        tag = "["+getClass().getName().replace(getClass().getPackage().getName()+".", "").replaceAll("\\$", "_")+"]";
    }

    final protected V view()
    {
        return view;
    }

    final protected void onNewThread(Runnable runnable)
    {
        new Thread(runnable).start();
    }
    final protected void together(Runnable runnableFirst, Runnable runnableSecond, Runnable... runnables)
    {
        new Thread(runnableFirst).start();
        new Thread(runnableSecond).start();
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