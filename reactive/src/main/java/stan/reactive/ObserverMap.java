package stan.reactive;

public class ObserverMap<T, U>
	implements Observer<T>
{
	private Observer<U> observer;
	private Func<T, U> func;
	
	public ObserverMap(Observer<U> o, Func<T, U> f)
	{
		observer = o;
		func = f;
	}

    public void next(T t)
    {
		try
		{
			observer.next(func.call(t));
		}
		catch(Throwable throwable)
		{
			error(throwable);
		}
	}
    public void error(Throwable t)
    {
    	observer.error(t);
    }
    public void complete()
    {
    	observer.complete();
    }
}