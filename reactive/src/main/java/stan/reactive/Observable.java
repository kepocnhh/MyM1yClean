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
                subscribeMap(new ObserverMap<>(observer, func));
            }
        };
    }

    public <U> Observable<U> flatMap(final Func<T, Observable<U>> func)
    {
        return new Observable<U>()
        {
            public void subscribe(final Observer<U> observer)
            {
                subscribeMap(new Observer<T>()
                {
                    @Override
                    public void next(T u)
                    {
                        func.call(u)
                            .subscribe(observer);
                    }
                    @Override
                    public void error(Throwable t)
                    {
                        observer.error(t);
                    }
                    @Override
                    public void complete()
                    {
                        observer.complete();
                    }
                });
            }
        };
    }

    private void subscribeMap(Observer<T> observer)
    {
        subscribe(observer);
    }
}