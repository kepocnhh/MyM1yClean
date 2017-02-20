package stan.reactive;

public abstract class SimpleObservable<T>
	extends Observable<T>
{
    public void subscribe(Observer<T> o)
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

    protected abstract T work() throws Exception;
}