package stan.mym1y.clean.units.mvp;

import android.util.Log;

public abstract class Presenter<VIEW>
{
    private VIEW view;
    public Presenter(VIEW v)
    {
        view = v;
    }
    final protected VIEW getView()
    {
        return view;
    }
    final protected void runOnNewThread(Runnable runnable)
    {
        new Thread(runnable).start();
    }
    final protected void runTogether(Runnable... runnables)
    {
        for(Runnable runnable : runnables)
        {
            runOnNewThread(runnable);
        }
    }
    final protected void log(String message)
    {
        Log.e(getClass().getName(), message);
    }
}