package stan.reactive.stream;

import stan.reactive.single.SingleObservable;
import stan.reactive.single.SingleObserver;

public abstract class StreamObservable<T>
        implements ObservableSource<T>
{
    public <U> SingleObservable<U> chainSingle(final StreamObserver<T> observer, final SingleObservable<U> observable)
    {
        return new SingleObservable<U>()
        {
            public void subscribe(final SingleObserver<U> o)
            {
                StreamObservable.this.subscribe(new StreamObserver<T>()
                {
                    public void next(T t)
                    {
                        observer.next(t);
                    }
                    public void complete()
                    {
                        observer.complete();
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