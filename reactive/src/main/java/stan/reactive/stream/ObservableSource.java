package stan.reactive.stream;

interface ObservableSource<T>
{
    void subscribe(StreamObserver<T> o);
}