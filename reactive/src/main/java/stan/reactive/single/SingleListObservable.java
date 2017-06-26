package stan.reactive.single;

import java.util.List;

import stan.reactive.stream.StreamObservable;
import stan.reactive.stream.StreamObserver;

public abstract class SingleListObservable<T>
    extends SingleObservable<List<T>>
{
    public StreamObservable<T> stream()
    {
        return new StreamObservable<T>()
        {
            public void subscribe(final StreamObserver<T> o)
            {
                SingleListObservable.this.subscribe(new SingleObserver<List<T>>()
                {
                    public void success(List<T> list)
                    {
                        for(T t: list)
                        {
                            o.next(t);
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
}