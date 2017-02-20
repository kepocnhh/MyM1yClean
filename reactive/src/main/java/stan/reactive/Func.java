package stan.reactive;

public interface Func<T, U>
{
	U call(T t);
}