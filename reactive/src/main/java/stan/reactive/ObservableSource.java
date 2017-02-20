package stan.reactive;

public interface ObservableSource<T>
{
    void subscribe(Observer<T> o);
}