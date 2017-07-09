package stan.mym1y.clean.modules.transactions;

import java.util.List;

import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.data.local.models.CashAccountsModels;
import stan.mym1y.clean.data.local.models.CurrenciesModels;

class AddNewTransactionModel
    implements AddNewTransactionContract.Model
{
    private final CashAccountsModels.CashAccounts cashAccounts;
    private final CurrenciesModels.Currencies currencies;
    private TransactionViewModel transactionViewModel;
    private Currency currency;

    AddNewTransactionModel(CashAccountsModels.CashAccounts cas, CurrenciesModels.Currencies cus)
    {
        cashAccounts = cas;
        currencies = cus;
        transactionViewModel = new TransactionView(-1, -1, 0, 0);
    }

    public List<CashAccount> getCashAccounts()
    {
        return cashAccounts.getAll();
    }
    public void setCashAccount(CashAccount cashAccount)
    {
        transactionViewModel = new TransactionView(cashAccount.id(), transactionViewModel.date(), transactionViewModel.count(), transactionViewModel.minorCount());
        currency = currencies.get(cashAccount.currencyCodeNumber());
    }
    public void setCount(int count, int minorCount)
    {
        transactionViewModel = new TransactionView(transactionViewModel.cashAccountId(), transactionViewModel.date(), count, minorCount);
    }
    public void setDate(long date)
    {
        transactionViewModel = new TransactionView(transactionViewModel.cashAccountId(), date, transactionViewModel.count(), transactionViewModel.minorCount());
    }
    public void checkNewTransaction()
            throws AddNewTransactionContract.ValidateDataException
    {
        if(transactionViewModel.count() == 0 && transactionViewModel.minorCount() == 0)
        {
            throw new AddNewTransactionContract.ValidateDataException(AddNewTransactionContract.ValidateDataException.Error.EMPTY_COUNT);
        }
    }
    public TransactionViewModel getNewTransaction()
    {
        return transactionViewModel;
    }
    public Currency getCurrency()
    {
        return currency;
    }
}