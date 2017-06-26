package stan.reactive.stream;

public interface StreamObserver<T>
{
    void next(T t);
    void complete();
    void error(Throwable t);

    abstract class Just<T>
        implements StreamObserver<T>
    {
        public void error(Throwable t)
        {
        }
    }
    abstract class Infinity<T>
        implements StreamObserver<T>
    {
        public void complete()
        {
        }
        public void error(Throwable t)
        {
        }
    }
}