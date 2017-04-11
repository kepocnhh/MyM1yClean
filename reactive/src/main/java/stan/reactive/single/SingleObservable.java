package stan.reactive.single;

import stan.reactive.Func;
import stan.reactive.Tuple;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.stream.StreamObservable;
import stan.reactive.stream.StreamObserver;

public abstract class SingleObservable<T>
        implements ObservableSource<T>
{
    public <U> SingleObservable<U> chain(final SingleObserver<T> observer, final SingleObservable<U> observable)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(final SingleObserver<U> o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        observer.success(t);
                        observable.subscribe(o);
                    }
                    public void error(Throwable t)
                    {
                        observer.error(t);
                    }
                });
            }
        };
    }
    public <U> SingleObservable<U> map(final Func<T, U> f)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(final SingleObserver<U> o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        o.success(f.call(t));
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
    public <U> SingleObservable<Tuple<T, U>> merge(final SingleObservable<U> observable)
    {
        return SingleObservable.this.flat(new Func<T, SingleObservable<Tuple<T, U>>>()
        {
            public SingleObservable<Tuple<T, U>> call(final T t)
            {
                return observable.flat(new Func<U, SingleObservable<Tuple<T, U>>>()
                {
                    public SingleObservable<Tuple<T, U>> call(final U u)
                    {
                        return new SingleObservable<Tuple<T, U>>()
                        {
                            public void subscribe(SingleObserver<Tuple<T, U>> o)
                            {
                                o.success(new Tuple<T, U>()
                                {
                                    public T first()
                                    {
                                        return t;
                                    }
                                    public U second()
                                    {
                                        return u;
                                    }
                                });
                            }
                        };
                    }
                });
            }
        });
    }
    public <U> SingleObservable<U> flat(final Func<T, SingleObservable<U>> f)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(final SingleObserver<U> o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        f.call(t).subscribe(o);
                    }
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
            public void subscribe(final NotifyObserver o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        o.notice();
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
    public NotifyObservable flatNotify(final Func<T, NotifyObservable> f)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        f.call(t).subscribe(o);
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }

    public <U> StreamObservable<U> chainStream(final SingleObserver<T> observer, final StreamObservable<U> observable)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(final StreamObserver<U> o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        observer.success(t);
                        observable.subscribe(o);
                    }
                    public void error(Throwable t)
                    {
                        observer.error(t);
                    }
                });
            }
        };
    }
    public <U> StreamObservable<U> flatStream(final Func<T, StreamObservable<U>> f)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(final StreamObserver<U> o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(T t)
                    {
                        f.call(t).subscribe(o);
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
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