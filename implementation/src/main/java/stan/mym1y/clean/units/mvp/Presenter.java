package stan.mym1y.clean.units.mvp;

import android.util.Log;

import java.util.Random;

public abstract class Presenter<VIEW>
{
    private final Random random = new Random();
    private final String tag;
    private final VIEW view;

    public Presenter(VIEW v)
    {
        view = v;
        tag = "["+getClass().getName().replace(getClass().getPackage().getName()+".", "")+"]";
    }

    final protected VIEW getView()
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
    final protected void chain(final Runnable... runnables)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                for(Runnable runnable : runnables)
                {
                    runnable.run();
                }
            }
        }).start();
    }

    final protected int nextInt()
    {
        return nextInt(Integer.MAX_VALUE-2)+1;
    }
    final protected int nextInt(int range)
    {
        return random.nextInt(range);
    }

    final protected void log(String message)
    {
        Log.e(tag, message);
    }
}