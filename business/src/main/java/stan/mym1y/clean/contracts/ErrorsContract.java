package stan.mym1y.clean.contracts;

public interface ErrorsContract
{
    class MyM1yException
            extends Exception
    {
        public MyM1yException(String message)
        {
            super(message);
        }
    }

    class NetworkErrorException
            extends MyM1yException
    {
        public NetworkErrorException(String link)
        {
            super("link - " + link);
        }
    }

    class AuthException
            extends MyM1yException
    {
        public AuthException(String message)
        {
            super(message);
        }
    }
    class UnauthorizedException
            extends AuthException
    {
        public UnauthorizedException(String message)
        {
            super(message);
        }
    }
    class InvalidDataException
            extends AuthException
    {
        public InvalidDataException(String message)
        {
            super(message);
        }
    }
    class ServerErrorException
            extends AuthException
    {
        public ServerErrorException(String message)
        {
            super(message);
        }
    }

    class UnknownErrorException
            extends MyM1yException
    {
        public UnknownErrorException(String message)
        {
            super(message);
        }
    }
}