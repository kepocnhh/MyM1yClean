package stan.reactive;

public abstract class Observable<T>
	implements ObservableSource<T>
{
	static public <U> Observable<U> just(final U... values)
	{
		return new Observable<U>()
		{
		    public void subscribe(Observer<U> o)
		    {
		        try
		        {
		        	for(U value : values)
		        	{
		            	o.next(value);
		        	}
		            o.complete();
		        }
		        catch(Throwable t)
		        {
		            o.error(t);
		        }
		    }
		};
	}

	public <U> Observable<U> map(final Func<T, U> func)
	{
		return new Observable<U>()
	    {
	        public void subscribe(Observer<U> observer)
	        {
	        	subscribeMap(observer, func);
	        }
	    };
	}
	private <U> void subscribeMap(Observer<U> observer, Func<T, U> func)
	{
    	subscribe(new ObserverMap<T, U>(observer, func));
	}
}