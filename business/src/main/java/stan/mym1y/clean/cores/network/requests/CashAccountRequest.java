package stan.mym1y.clean.cores.network.requests;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.transactions.Transaction;

public interface CashAccountRequest
{
    CashAccount cashAccount();
    List<Transaction> transactions();
}