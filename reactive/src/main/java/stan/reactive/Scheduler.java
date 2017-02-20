package stan.reactive;

public interface Scheduler
{
	Scheduler SYNC = new Scheduler()
    {
        public void run(Runnable runnable)
        {
            runnable.run();
        }
    };
	Scheduler NEW = new Scheduler()
    {
        public void run(Runnable runnable)
        {
            new Thread(runnable).start();
        }
    };

	void run(Runnable runnable);
}