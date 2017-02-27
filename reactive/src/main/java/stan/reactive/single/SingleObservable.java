package stan.reactive.single;

import stan.reactive.Func;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;

public abstract class SingleObservable<T>
        implements ObservableSource<T>
{
    public <U> SingleObservable<U> map(final Func<T, U> func)
    {
        return new SingleObservable<U>()
        {
            @Override
            public void subscribe(final SingleObserver<U> o)
            {
                subscribeMap(new SingleObserver<T>()
                {
                    @Override
                    public void success(T t)
                    {
                        o.success(func.call(t));
                    }
                    @Override
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
    public <U> SingleObservable<U> flat(final Func<T, SingleObservable<U>> func)
    {
        return new SingleObservable<U>()
        {
            @Override
            public void subscribe(final SingleObserver<U> o)
            {
                subscribeMap(new SingleObserver<T>()
                {
                    @Override
                    public void success(T u)
                    {
                        func.call(u).subscribe(o);
                    }
                    @Override
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }

    public NotifyObservable mapNotify()
    {
        return new NotifyObservable()
        {
            @Override
            public void subscribe(final NotifyObserver o)
            {
                subscribeMap(new SingleObserver<T>()
                {
                    @Override
                    public void success(T t)
                    {
                        o.notice();
                    }
                    @Override
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }

    private void subscribeMap(SingleObserver<T> observer)
    {
        subscribe(observer);
    }

    static public abstract class Work<K>
            extends SingleObservable<K>
    {
        public void subscribe(SingleObserver<K> o)
        {
            try
            {
                o.success(work());
            }
            catch(Throwable t)
            {
                o.error(t);
            }
        }

        protected abstract K work() throws Throwable;
    }
}