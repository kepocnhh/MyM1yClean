package stan.mym1y.clean.modules.network.requests;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.network.requests.CashAccountRequest;
import stan.mym1y.clean.cores.transactions.Transaction;

public class CashAccountRequestData
    implements CashAccountRequest
{
    private final CashAccount cashAccount;
    private final List<Transaction> transactions;

    public CashAccountRequestData(CashAccount cashAccount, List<Transaction> transactions)
    {
        this.cashAccount = cashAccount;
        this.transactions = transactions;
    }

    public CashAccount cashAccount()
    {
        return cashAccount;
    }
    public List<Transaction> transactions()
    {
        return transactions;
    }
}