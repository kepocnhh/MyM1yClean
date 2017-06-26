package stan.reactive.functions;

public interface Func<T, U>
{
	U call(T t);
}