package stan.mym1y.clean.cores.cashaccounts;

public interface CashAccount
{
    long id();
    String title();

    interface Extra
    {
        int balance();
    }
}