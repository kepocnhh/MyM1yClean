package stan.mym1y.clean.cores.transactions;

public interface TransactionViewModel
{
    long cashAccountId();
    long date();
    boolean income();
    int count();
    int minorCount();
}