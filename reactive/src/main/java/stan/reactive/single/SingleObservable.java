package stan.reactive.single;

import java.util.List;

import stan.reactive.functions.Apply;
import stan.reactive.functions.Func;
import stan.reactive.Scheduler;
import stan.reactive.Subscribable;
import stan.reactive.Tuple;
import stan.reactive.functions.Worker;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.notify.NotifyObserver;
import stan.reactive.stream.StreamObservable;
import stan.reactive.stream.StreamObserver;

public abstract class SingleObservable<T>
        implements Subscribable<SingleObserver<T>>
{
    static public <U> SingleObservable<U> create(final U u)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(SingleObserver<U> o)
            {
                o.success(u);
            }
        };
    }
    static public <U> SingleListObservable<U> create(final List<U> list)
    {
        return new SingleListObservable<U>()
        {
            public void subscribe(SingleObserver<List<U>> o)
            {
                o.success(list);
            }
        };
    }
    static public <U> SingleObservable<U> create(final Apply<U> apply)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(SingleObserver<U> o)
            {
                o.success(apply.apply());
            }
        };
    }
    static public <U> SingleObservable<U> create(final Worker<U> worker)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(SingleObserver<U> o)
            {
                try
                {
                    o.success(worker.work());
                }
                catch(Throwable throwable)
                {
                    o.error(throwable);
                }
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
    public <U> StreamObservable<U> stream(final Func<T, StreamObservable<U>> f)
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
    public NotifyObservable notice()
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
    public <U> StreamObservable<U> chain(final SingleObserver<T> observer, final StreamObservable<U> observable)
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
    public NotifyObservable chain(final SingleObserver<T> observer, final NotifyObservable observable)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
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

    public SingleObservable<T> subscribeOn(final Scheduler scheduler)
    {
        return new SingleObservable<T>()
        {
            public void subscribe(final SingleObserver<T> o)
            {
                scheduler.run(new Runnable()
                {
                    public void run()
                    {
                        SingleObservable.this.subscribe(o);
                    }
                });
            }
        };
    }
    public SingleObservable<T> observeOn(final Scheduler scheduler)
    {
        return new SingleObservable<T>()
        {
            public void subscribe(final SingleObserver<T> o)
            {
                SingleObservable.this.subscribe(new SingleObserver<T>()
                {
                    public void success(final T t)
                    {
                        scheduler.run(new Runnable()
                        {
                            public void run()
                            {
                                o.success(t);
                            }
                        });
                    }
                    public void error(final Throwable t)
                    {
                        scheduler.run(new Runnable()
                        {
                            public void run()
                            {
                                o.error(t);
                            }
                        });
                    }
                });
            }
        };
    }

}