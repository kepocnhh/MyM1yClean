package stan.reactive.stream;

import java.util.ArrayList;
import java.util.List;

import stan.reactive.Scheduler;
import stan.reactive.functions.Apply;
import stan.reactive.functions.Func;
import stan.reactive.Subscribable;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

public abstract class StreamObservable<T>
        implements Subscribable<StreamObserver<T>>
{
    static public <U> StreamObservable<U> create(final U u, final U... array)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(StreamObserver<U> o)
            {
                o.next(u);
                for(U a: array)
                {
                    o.next(a);
                }
                o.complete();
            }
        };
    }
    static public <U> StreamObservable<U> create(final List<U> list)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(StreamObserver<U> o)
            {
                for(U u: list)
                {
                    o.next(u);
                }
                o.complete();
            }
        };
    }
    static public <U> StreamObservable<U> create(final Apply<List<U>> apply)
    {
        return create(apply.apply());
    }

    public <U> StreamObservable<U> map(final Func<T, U> f)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(final StreamObserver<U> o)
            {
                StreamObservable.this.subscribe(new StreamObserver<T>()
                {
                    public void next(T t)
                    {
                        o.next(f.call(t));
                    }
                    public void complete()
                    {
                        o.complete();
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
    public SingleObservable<List<T>> single()
    {
        return new SingleObservable<List<T>>()
        {
            public void subscribe(final SingleObserver<List<T>> o)
            {
                StreamObservable.this.subscribe(new StreamObserver<T>()
                {
                    private final List<T> list = new ArrayList<T>();

                    public void next(T t)
                    {
                        list.add(t);
                    }
                    public void complete()
                    {
                        o.success(list);
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
    public StreamObservable<T> filter(final Func<T, Boolean> f)
    {
        return new StreamObservable<T>()
        {
            public void subscribe(final StreamObserver<T> o)
            {
                StreamObservable.this.subscribe(new StreamObserver<T>()
                {
                    public void next(T t)
                    {
                        if(f.call(t))
                            o.next(t);
                    }
                    public void complete()
                    {
                        o.complete();
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }

    public StreamObservable<T> subscribeOn(final Scheduler scheduler)
    {
        return new StreamObservable<T>()
        {
            public void subscribe(final StreamObserver<T> o)
            {
                scheduler.run(new Runnable()
                {
                    public void run()
                    {
                        StreamObservable.this.subscribe(o);
                    }
                });
            }
        };
    }
    public StreamObservable<T> observeOn(final Scheduler scheduler)
    {
        return new StreamObservable<T>()
        {
            public void subscribe(final StreamObserver<T> o)
            {
                StreamObservable.this.subscribe(new StreamObserver<T>()
                {
                    public void next(final T t)
                    {
                        scheduler.run(new Runnable()
                        {
                            public void run()
                            {
                                o.next(t);
                            }
                        });
                    }
                    public void complete()
                    {
                        scheduler.run(new Runnable()
                        {
                            public void run()
                            {
                                o.complete();
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