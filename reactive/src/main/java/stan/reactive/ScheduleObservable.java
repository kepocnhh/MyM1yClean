package stan.reactive;

public abstract class ScheduleObservable<T>
	extends Observable<T>
{
    private Scheduler scheduler;

    public ScheduleObservable(Scheduler s)
    {
        scheduler = s;
    }
    public ScheduleObservable()
    {
        this(Scheduler.SYNC);
    }

    public void subscribe(final Observer<T> o)
    {
        scheduler.run(new Runnable()
        {
            public void run()
            {
            	doOnSubscribe(o);
            }
        });
    }

    protected abstract void doOnSubscribe(Observer<T> o);
}