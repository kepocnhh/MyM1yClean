package stan.reactive.notify;

import java.util.List;

import stan.reactive.Scheduler;
import stan.reactive.Subscribable;
import stan.reactive.functions.Action;
import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;
import stan.reactive.stream.StreamObservable;
import stan.reactive.stream.StreamObserver;

public abstract class NotifyObservable
        implements Subscribable<NotifyObserver>
{
    static public NotifyObservable create(final Runnable runnable)
    {
        return new NotifyObservable()
        {
            public void subscribe(NotifyObserver o)
            {
                runnable.run();
                o.notice();
            }
        };
    }
    static public NotifyObservable create(final Action action)
    {
        return new NotifyObservable()
        {
            public void subscribe(NotifyObserver o)
            {
                try
                {
                    action.run();
                    o.notice();
                }
                catch(Throwable throwable)
                {
                    o.error(throwable);
                }
            }
        };
    }

    public NotifyObservable chain(final NotifyObserver observer, final NotifyObservable observable)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        observer.notice();
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
    public <U> SingleObservable<U> chain(final NotifyObserver observer, final SingleObservable<U> observable)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(final SingleObserver<U> o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        observer.notice();
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
    public <U> StreamObservable<U> chain(final NotifyObserver observer, final StreamObservable<U> observable)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(final StreamObserver<U> o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        observer.notice();
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
    public <U> SingleObservable<U> single(final U u)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(final SingleObserver<U> o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        o.success(u);
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }
    public <U> StreamObservable<U> stream(final List<U> list)
    {
        return new StreamObservable<U>()
        {
            public void subscribe(final StreamObserver<U> o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        for(U u: list)
                        {
                            o.next(u);
                        }
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
    public NotifyObservable merge(final NotifyObservable observable)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        observable.subscribe(new NotifyObserver()
                        {
                            public void notice()
                            {
                                o.notice();
                            }
                            public void error(Throwable t)
                            {
                                o.error(t);
                            }
                        });
                    }
                    public void error(Throwable t)
                    {
                        o.error(t);
                    }
                });
            }
        };
    }

    public NotifyObservable subscribeOn(final Scheduler scheduler)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                scheduler.run(new Runnable()
                {
                    public void run()
                    {
                        NotifyObservable.this.subscribe(o);
                    }
                });
            }
        };
    }
    public NotifyObservable observeOn(final Scheduler scheduler)
    {
        return new NotifyObservable()
        {
            public void subscribe(final NotifyObserver o)
            {
                NotifyObservable.this.subscribe(new NotifyObserver()
                {
                    public void notice()
                    {
                        scheduler.run(new Runnable()
                        {
                            public void run()
                            {
                                o.notice();
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