package stan.reactive;

public interface Subscribable<O>
{
    void subscribe(O o);
}