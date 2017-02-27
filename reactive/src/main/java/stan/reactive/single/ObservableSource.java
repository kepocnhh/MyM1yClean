package stan.reactive.single;

interface ObservableSource<T>
{
    void subscribe(SingleObserver<T> o);
}