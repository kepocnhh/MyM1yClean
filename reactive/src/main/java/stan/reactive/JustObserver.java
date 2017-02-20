package stan.reactive;

public abstract class JustObserver<T>
	implements Observer<T>
{
	public void error(Throwable t)
	{
	}
	public void complete()
	{
	}
}