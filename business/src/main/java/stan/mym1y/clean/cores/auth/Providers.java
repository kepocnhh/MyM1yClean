package stan.mym1y.clean.cores.auth;

public interface Providers
{
    enum Type
    {
        NONE("none"),
        GOOGLE("google.com");

        public final String value;

        Type(String v)
        {
            value = v;
        }
    }
}