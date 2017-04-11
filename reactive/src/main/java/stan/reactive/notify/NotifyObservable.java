package stan.reactive.notify;

public abstract class NotifyObservable
        implements ObservableSource
{
    public void run()
    {
        subscribe(new NotifyObserver.Just()
        {
            public void notice()
            {
            }
        });
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
}