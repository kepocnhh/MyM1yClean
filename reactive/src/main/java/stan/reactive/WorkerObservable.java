package stan.reactive;

public abstract class WorkerObservable<T>
	extends ScheduleObservable<T>
{
    public WorkerObservable(Scheduler s)
    {
        super(s);
    }
    public WorkerObservable()
    {
        super();
    }

    public void doOnSubscribe(Observer<T> o)
    {
        try
        {
            o.next(work());
            o.complete();
        }
        catch(Throwable t)
        {
            o.error(t);
        }
    }

    protected abstract T work();
}