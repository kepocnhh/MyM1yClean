package stan.reactive.functions;

public interface Worker<T>
{
    T work() throws Throwable;
}