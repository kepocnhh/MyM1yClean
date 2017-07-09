package stan.mym1y.clean.modules.transactions;

import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.units.mvp.ModelPresenter;
import stan.reactive.Scheduler;
import stan.reactive.Tuple;
import stan.reactive.functions.Apply;
import stan.reactive.single.SingleObservable;

class AddNewTransactionPresenter
    extends ModelPresenter<AddNewTransactionContract.View, AddNewTransactionContract.Model>
    implements AddNewTransactionContract.Presenter
{
    AddNewTransactionPresenter(AddNewTransactionContract.View v, AddNewTransactionContract.Model m)
    {
        super(v, m);
    }

    public void update()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                view().cashAccounts(model().getCashAccounts());
            }
        });
    }

    public void setCashAccount(CashAccount cashAccount)
    {
        if(cashAccount.id() != model().getNewTransaction().cashAccountId())
        {
            model().setCashAccount(cashAccount);
            model().setCount(0, 0);
            view().updateTransaction(model().getNewTransaction(), model().getCurrency());
        }
    }
    public void setCount(int count, int minorCount)
    {
        model().setCount(count, minorCount);
        view().updateTransaction(model().getNewTransaction(), model().getCurrency());
    }
    public void setDate(long date)
    {
        model().setDate(date);
    }
    public SingleObservable<Tuple<TransactionViewModel, Currency>> updateTransaction()
    {
        return SingleObservable.create(new Apply<Tuple<TransactionViewModel, Currency>>()
        {
            final TransactionViewModel transactionViewModel = model().getNewTransaction();
            final Currency currency = model().getCurrency();
            public Tuple<TransactionViewModel, Currency> apply()
            {
                return new Tuple<TransactionViewModel, Currency>()
                {
                    public TransactionViewModel first()
                    {
                        return transactionViewModel;
                    }
                    public Currency second()
                    {
                        return currency;
                    }
                };
            }
        }).subscribeOn(Scheduler.NEW).observeOn(viewScheduler);
    }
    public void addNewTransaction()
    {
        onNewThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    model().checkNewTransaction();
                    view().addNewTransaction(model().getNewTransaction());
                }
                catch(AddNewTransactionContract.ValidateDataException e)
                {
                    view().error(e);
                }
            }
        });
    }
}
