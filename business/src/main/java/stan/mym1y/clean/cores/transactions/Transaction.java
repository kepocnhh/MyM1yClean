package stan.mym1y.clean.cores.transactions;

public interface Transaction
{
    long id();
    long cashAccountId();
    long date();
    int count();
    int minorCount();

    interface Extra
    {
        String cashAccountTitle();
    }
}