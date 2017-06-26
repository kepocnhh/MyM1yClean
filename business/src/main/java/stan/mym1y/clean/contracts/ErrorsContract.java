package stan.mym1y.clean.contracts;

public interface ErrorsContract
{
    class MyM1yException
            extends Exception
    {
        public MyM1yException()
        {
            super();
        }
        public MyM1yException(Throwable t)
        {
            super(t);
        }
        public MyM1yException(String message)
        {
            super(message);
        }
    }

    class DataNotExistException
            extends MyM1yException
    {
    }
    class UnauthorizedException
            extends MyM1yException
    {
    }
    class NetworkException
            extends MyM1yException
    {
        public NetworkException(String link)
        {
            super("link - " + link);
        }
    }
}