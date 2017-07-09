package stan.mym1y.clean.units.mvp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import stan.reactive.Scheduler;

public abstract class Presenter<VIEW>
{
    static private final Handler viewHandler = new Handler(Looper.getMainLooper());
    static protected final Scheduler viewScheduler = new Scheduler()
    {
        public void run(Runnable runnable)
        {
            viewHandler.post(runnable);
        }
    };
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