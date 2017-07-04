package stan.mym1y.clean.cores.cashaccounts;

public interface CashAccount
{
    long id();
    String uuid();
    String currencyCodeNumber();
    String title();

    interface Extra
    {
        int balance();
    }
}