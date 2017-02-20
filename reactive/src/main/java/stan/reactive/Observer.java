package stan.reactive;

public interface Observer<T>
{
	void next(T t);
	void error(Throwable t);
	void complete();
}