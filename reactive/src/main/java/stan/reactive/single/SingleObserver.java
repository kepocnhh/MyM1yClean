package stan.reactive.single;

public interface SingleObserver<T>
{
    void success(T t);
    void error(Throwable t);

    abstract class Just<T>
            implements SingleObserver<T>
    {
        public void error(Throwable t)
        {
        }
    }
}