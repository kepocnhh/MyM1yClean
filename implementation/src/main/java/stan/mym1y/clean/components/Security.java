package stan.mym1y.clean.components;

public interface Security
{
    String sha512(String data);
    int newUniqueId();
    String newUUID();
}