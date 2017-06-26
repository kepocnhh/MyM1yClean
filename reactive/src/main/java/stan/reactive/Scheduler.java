package stan.reactive;

public interface Scheduler
{
    Scheduler NEW = new Scheduler()
    {
        public void run(Runnable runnable)
        {
            new Thread(runnable).start();
        }
    };

    void run(Runnable runnable);
}