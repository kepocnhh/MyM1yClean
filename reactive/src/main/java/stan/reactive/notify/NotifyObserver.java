package stan.reactive.notify;

public interface NotifyObserver
{
    void notice();
    void error(Throwable t);

    abstract class Just
        implements NotifyObserver
    {
        public void error(Throwable t)
        {
        }
    }
}