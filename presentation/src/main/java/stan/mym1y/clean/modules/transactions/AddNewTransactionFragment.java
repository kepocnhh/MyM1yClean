package stan.mym1y.clean.modules.transactions;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.modules.transactions.cashaccounts.CashAccountsList;
import stan.mym1y.clean.units.fragments.UtilFragment;
import stan.reactive.Tuple;
import stan.reactive.single.SingleObserver;

public class AddNewTransactionFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(AddNewTransactionContract.Behaviour b)
    {
        AddNewTransactionFragment fragment = new AddNewTransactionFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private AddNewTransactionContract.Presenter presenter;
    private final AddNewTransactionContract.View view = new AddNewTransactionContract.View()
    {
        public void cashAccounts(final List<CashAccount> cashAccounts)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    cashAccountsList.swapData(cashAccounts);
                }
            });
        }
        public void addNewTransaction(TransactionViewModel transactionViewModel)
        {
            behaviour.newTransaction(transactionViewModel);
        }
        public void updateTransaction(TransactionViewModel transactionViewModel, Currency currency)
        {
            setCounts(currency, transactionViewModel);
        }
        public void error(AddNewTransactionContract.ValidateDataException exception)
        {
            toast("Count must be not zero!");
        }
    };
    private AddNewTransactionContract.Behaviour behaviour;

    private CashAccountsList cashAccountsList;

    private View background;
    private TextView set_cash_account_text;
    private TextView enter_count_text;
    private TextView count_text;
    private TextView add;
    private TextView cancel;

    private final EnterCountFragment.Listener enterCountListener = new EnterCountFragment.Listener()
    {
        public void confirm(boolean income, int count, int minorCount)
        {
            presenter.setCount(income, count, minorCount);
            clear(R.id.enter_count_subscreen);
            setSystemUiVisibilityLight(!currentTheme.isDarkTheme());
            setStatusBarColor(currentTheme.colors().background());
        }
        public void cancel()
        {
            clear(R.id.enter_count_subscreen);
            setSystemUiVisibilityLight(!currentTheme.isDarkTheme());
            setStatusBarColor(currentTheme.colors().background());
        }
    };
    private String nothing_label;
    private Theme currentTheme;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.add:
                presenter.addNewTransaction();
                break;
            case R.id.cancel:
                behaviour.cancel();
                break;
            case R.id.count_text:
                presenter.updateTransaction().subscribe(new SingleObserver.Just<Tuple<TransactionViewModel, Currency>>()
                {
                    public void success(Tuple<TransactionViewModel, Currency> tuple)
                    {
                        replace(R.id.enter_count_subscreen, EnterCountFragment.newInstance(enterCountListener,
                                tuple.second().minorUnitType(),
                                tuple.first().income(),
                                tuple.first().count(),
                                tuple.first().minorCount()));
                    }
                });
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.add_new_transaction_screen;
    }
    protected void initViews(View v)
    {
        background = findView(R.id.background);
        set_cash_account_text = findView(R.id.set_cash_account_text);
        enter_count_text = findView(R.id.enter_count_text);
        count_text = findView(R.id.count_text);
        add = findView(R.id.add);
        cancel = findView(R.id.cancel);
        setClickListener(add, cancel, count_text);
    }
    protected void init()
    {
        nothing_label = getActivity().getResources().getString(R.string.nothing_label);
        cashAccountsList = new CashAccountsList(getActivity(), (RecyclerView)findView(R.id.cash_accounts), App.component().themeSwitcher().theme(), new CashAccountsList.Listener()
        {
            public void cashAccount(CashAccount cashAccount)
            {
                presenter.setCashAccount(cashAccount);
            }
        });
        presenter = new AddNewTransactionPresenter(view, new AddNewTransactionModel(App.component().dataLocal().cashAccountsAccess().cashAccounts(),
                App.component().dataLocal().currenciesAccess().currencies()));
        setTheme(App.component().themeSwitcher().theme());
        presenter.update();
        presenter.setDate(System.currentTimeMillis());
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        background.setBackgroundColor(currentTheme.colors().background());
        set_cash_account_text.setTextColor(currentTheme.colors().foreground());
        enter_count_text.setTextColor(currentTheme.colors().foreground());
        add.setTextColor(currentTheme.colors().accent());
        cancel.setTextColor(currentTheme.colors().foreground());
    }

    private void setCounts(Currency currency, TransactionViewModel transaction)
    {
        if(transaction.count() == 0 && transaction.minorCount() == 0)
        {
            count_text.setText(nothing_label);
            count_text.setTextColor(currentTheme.colors().neutral());
            return;
        }
        String left = transaction.income() ? "+" : "-";
        String middle = String.valueOf(Math.abs(transaction.count()));
        String right = currency.codeName();
        switch(currency.minorUnitType())
        {
            case TEN:
                middle += "." + String.valueOf(transaction.minorCount());
                break;
            case HUNDRED:
                middle += "." + (transaction.minorCount() < 10 ? "0" + transaction.minorCount() : transaction.minorCount());
                break;
        }
        count_text.setText(left + middle + " " + right);
        count_text.setTextColor(transaction.income() ? currentTheme.colors().positive() : currentTheme.colors().negative());
    }
}