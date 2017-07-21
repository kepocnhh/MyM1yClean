package stan.mym1y.clean.modules.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.contracts.cashaccounts.AddNewCashAccountContract;
import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.modules.cashaccounts.AddNewCashAccountFragment;
import stan.mym1y.clean.modules.cashaccounts.DeleteCashAccountConfirmDialog;
import stan.mym1y.clean.modules.main.balances.BalancesList;
import stan.mym1y.clean.modules.main.cashaccounts.CashAccountsList;
import stan.mym1y.clean.modules.main.transactions.TransactionsList;
import stan.mym1y.clean.modules.transactions.AddNewTransactionFragment;
import stan.mym1y.clean.modules.transactions.DeleteTransactionConfirmDialog;
import stan.mym1y.clean.units.fragments.UtilFragment;
import stan.reactive.Tuple;

public class MainFragment
        extends UtilFragment
{
    static public UtilFragment newInstance(MainContract.Behaviour b)
    {
        MainFragment fragment = new MainFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private MainContract.Presenter presenter;
    private final MainContract.View view = new MainContract.View()
    {
        public void error(ErrorsContract.NetworkException e)
        {
//            showToast("NetworkException " + e.getMessage());
        }
        public void error(ErrorsContract.UnauthorizedException e)
        {
//            showToast("UnauthorizedException");
            behaviour.unauthorized();
        }
        public void error()
        {
            toast("Unknown Error!");
        }
        public void emptyCashAccounts()
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    cash_accounts_container.setVisibility(View.GONE);
                    transactionsList.hide();
                    empty_cash_accounts.setVisibility(View.VISIBLE);
                    empty_transactions.setVisibility(View.GONE);
                    new_transaction.setVisibility(View.GONE);
                    balance_text.setVisibility(View.GONE);
                    balance_value.setVisibility(View.GONE);
                    balancesList.hide();
                }
            });
        }
        public void emptyTransactions(final List<Tuple<CashAccount, CashAccount.Extra>> cashAccounts)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    cash_accounts_container.setVisibility(View.VISIBLE);
                    transactionsList.hide();
                    empty_cash_accounts.setVisibility(View.GONE);
                    empty_transactions.setVisibility(View.VISIBLE);
                    new_transaction.setVisibility(View.VISIBLE);
                    cashAccountsList.swapData(cashAccounts);
                    balance_text.setVisibility(View.GONE);
                    balance_value.setVisibility(View.GONE);
                    balancesList.hide();
                }
            });
        }
        public void update(final List<Tuple<CashAccount, CashAccount.Extra>> cashAccounts, final List<Tuple<Transaction, Transaction.Extra>> transactions)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    cash_accounts_container.setVisibility(View.VISIBLE);
                    empty_cash_accounts.setVisibility(View.GONE);
                    empty_transactions.setVisibility(View.GONE);
                    new_transaction.setVisibility(View.VISIBLE);
                    transactionsList.swapData(transactions);
                    cashAccountsList.swapData(cashAccounts);
                }
            });
        }
        public void update(final CashAccount.Extra balance)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    balance_text.setVisibility(View.VISIBLE);
                    balance_text.setText(balance_label + ":");
                    balance_value.setVisibility(View.VISIBLE);
                    balancesList.hide();
                    if(balance.count() == 0 && balance.minorCount() == 0)
                    {
                        balance_value.setText(nothing_label);
                        balance_value.setTextColor(currentTheme.colors().neutral());
                        return;
                    }
                    String left = balance.income() ? "+" : "-";
                    String middle = String.valueOf(Math.abs(balance.count()));
                    String right = balance.currency().codeName();
                    switch(balance.currency().minorUnitType())
                    {
                        case TEN:
                            middle += "." + String.valueOf(balance.minorCount());
                            break;
                        case HUNDRED:
                            middle += "." + (balance.minorCount() < 10 ? "0" + balance.minorCount() : balance.minorCount());
                            break;
                    }
                    balance_value.setText(left + middle + " " + right);
                    balance_value.setTextColor(balance.income() ? currentTheme.colors().positive() : currentTheme.colors().negative());
                }
            });
        }
        public void update(final List<CashAccount.Extra> balance)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    balance_text.setVisibility(View.VISIBLE);
                    balance_text.setText(balance_label + ":");
                    balance_value.setVisibility(View.GONE);
                    balancesList.swapData(balance);
                }
            });
        }
    };
    private MainContract.Behaviour behaviour;

    private View toolbar_divider;
    private View cash_accounts_divider;
    private View divider_bottom;
    private TextView empty_transactions_text;
    private TextView empty_cash_accounts_text;
    private TextView add_new_cash_account;

    private View cash_accounts_container;
    private View empty_cash_accounts;
    private View empty_transactions;
    private TextView balance_text;
    private ImageView new_transaction;
    private TextView balance_value;

    private CashAccountsList cashAccountsList;
    private TransactionsList transactionsList;
    private BalancesList balancesList;
    private String balance_label;
    private String nothing_label;
    private Theme currentTheme;

    private final AddNewCashAccountContract.Behaviour addNewCashAccountBehaviour = new AddNewCashAccountContract.Behaviour()
    {
        public void newCashAccount(CashAccountViewModel cashAccountViewModel)
        {
            presenter.add(cashAccountViewModel);
            clear(R.id.add_subscreen);
        }
        public void cancel()
        {
            clear(R.id.add_subscreen);
        }
    };
    private final AddNewTransactionContract.Behaviour addNewTransactionBehaviour = new AddNewTransactionContract.Behaviour()
    {
        public void newTransaction(TransactionViewModel transactionViewModel)
        {
            presenter.add(transactionViewModel);
            clear(R.id.add_subscreen);
        }
        public void cancel()
        {
            clear(R.id.add_subscreen);
        }
    };

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.new_transaction:
                newTransaction();
                break;
            case R.id.add_new_cash_account:
                log("try add new cash account");
                newCashAccount();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.main_screen;
    }
    protected void initViews(View v)
    {
        toolbar_divider = findView(R.id.toolbar_divider);
        cash_accounts_divider = findView(R.id.cash_accounts_divider);
        divider_bottom = findView(R.id.divider_bottom);
        empty_transactions_text = findView(R.id.empty_transactions_text);
        empty_cash_accounts_text = findView(R.id.empty_cash_accounts_text);
        add_new_cash_account = findView(R.id.add_new_cash_account);
        //
        cash_accounts_container = findView(R.id.cash_accounts_container);
        empty_cash_accounts = findView(R.id.empty_cash_accounts);
        empty_transactions = findView(R.id.empty_transactions);
        balance_text = findView(R.id.balance_text);
        new_transaction = findView(R.id.new_transaction);
        balance_value = findView(R.id.balance_value);
        setClickListener(new_transaction, add_new_cash_account);
    }
    protected void init()
    {
        presenter = new MainPresenter(view, new MainModel(App.component().dataLocal().transactionsAccess().transactions(),
                App.component().dataLocal().cashAccountsAccess().cashAccounts(),
                App.component().dataLocal().currenciesAccess().currencies(),
                App.component().security(),
                App.component().settings(),
                App.component().jsonConverter(),
                App.component().dataRemote().authApi(),
                App.component().dataRemote().privateDataApi()));
        cashAccountsList = new CashAccountsList(getActivity(), (RecyclerView)findView(R.id.cash_accounts), App.component().themeSwitcher().theme(), new CashAccountsList.Listener()
        {
            public void delete(CashAccount cashAccount)
            {
                deleteCashAccount(cashAccount);
            }
            public void cashAccount(CashAccount cashAccount)
            {
                toast("id " + cashAccount.id());
            }
            public void addNewCashAccount()
            {
                newCashAccount();
            }
        });
        transactionsList = new TransactionsList(getActivity(), (RecyclerView)findView(R.id.transactions), App.component().themeSwitcher().theme(), new TransactionsList.Listener()
        {
            public void delete(Transaction transaction)
            {
                deleteTransaction(transaction);
            }
        });
        balancesList = new BalancesList(getActivity(), (Spinner)findView(R.id.balances), App.component().themeSwitcher().theme());
        balance_label = getActivity().getResources().getString(R.string.balance_label);
        nothing_label = getActivity().getResources().getString(R.string.nothing_label);
        cash_accounts_container.setVisibility(View.GONE);
        empty_cash_accounts.setVisibility(View.GONE);
        empty_transactions.setVisibility(View.GONE);
        transactionsList.hide();
        new_transaction.setVisibility(View.GONE);
        balance_text.setVisibility(View.GONE);
        balance_value.setVisibility(View.GONE);
        balancesList.hide();
        setTheme(App.component().themeSwitcher().theme());
        presenter.update();
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        setStatusBarColor(theme.colors().background());
        setSystemUiVisibilityLight(!theme.isDarkTheme());
        toolbar_divider.setBackgroundColor(theme.colors().foreground());
        cash_accounts_divider.setBackgroundColor(theme.colors().foreground());
        if(theme.isDarkTheme())
        {
            divider_bottom.setVisibility(View.VISIBLE);
            divider_bottom.setBackgroundColor(theme.colors().foreground());
        }
        else
        {
            divider_bottom.setVisibility(View.GONE);
        }
        empty_transactions_text.setTextColor(theme.colors().foreground());
        empty_cash_accounts_text.setTextColor(theme.colors().foreground());
        add_new_cash_account.setTextColor(theme.colors().accent());
        balance_text.setTextColor(theme.colors().foreground());
        new_transaction.setColorFilter(theme.colors().foreground());
    }

    private void newCashAccount()
    {
        replace(R.id.add_subscreen, AddNewCashAccountFragment.newInstance(addNewCashAccountBehaviour));
    }
    private void deleteCashAccount(final CashAccount cashAccount)
    {
        DeleteCashAccountConfirmDialog.newInstance(new DeleteCashAccountConfirmDialog.Listener()
        {
            public void confirm()
            {
                presenter.delete(cashAccount);
            }
        }).show(getFragmentManager(), DeleteCashAccountConfirmDialog.class.getName());
    }

    private void newTransaction()
    {
        replace(R.id.add_subscreen, AddNewTransactionFragment.newInstance(addNewTransactionBehaviour));
    }
    private void deleteTransaction(final Transaction transaction)
    {
        DeleteTransactionConfirmDialog.newInstance(new DeleteTransactionConfirmDialog.Listener()
        {
            public void confirm()
            {
                presenter.delete(transaction);
            }
        }).show(getFragmentManager(), DeleteTransactionConfirmDialog.class.getName());
    }
}