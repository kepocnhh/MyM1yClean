package stan.mym1y.clean.modules.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import stan.mym1y.clean.modules.cashaccounts.AddNewCashAccountFragment;
import stan.mym1y.clean.modules.cashaccounts.DeleteCashAccountConfirmDialog;
import stan.mym1y.clean.modules.main.balances.BalancesAdapter;
import stan.mym1y.clean.modules.main.cashaccounts.CashAccountsAdapter;
import stan.mym1y.clean.modules.main.transactions.TransactionsAdapter;
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
            behaviour.logout();
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
                    transactions.setVisibility(View.GONE);
                    empty_cash_accounts.setVisibility(View.VISIBLE);
                    empty_transactions.setVisibility(View.GONE);
                    new_transaction.setVisibility(View.GONE);
                    balance_text.setVisibility(View.GONE);
                    balance_value.setVisibility(View.GONE);
                    balances.setVisibility(View.GONE);
                    balancesAdapter.swapData(null);
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
                    transactions.setVisibility(View.GONE);
                    empty_cash_accounts.setVisibility(View.GONE);
                    empty_transactions.setVisibility(View.VISIBLE);
                    new_transaction.setVisibility(View.VISIBLE);
                    cashAccountsAdapter.swapData(cashAccounts);
                    cashAccountsAdapter.notifyDataSetChanged();
                    balance_text.setVisibility(View.GONE);
                    balance_value.setVisibility(View.GONE);
                    balances.setVisibility(View.GONE);
                    balancesAdapter.swapData(null);
                }
            });
        }
        public void update(final List<Tuple<CashAccount, CashAccount.Extra>> cas, final List<Tuple<Transaction, Transaction.Extra>> ts)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    cash_accounts_container.setVisibility(View.VISIBLE);
                    transactions.setVisibility(View.VISIBLE);
                    empty_cash_accounts.setVisibility(View.GONE);
                    empty_transactions.setVisibility(View.GONE);
                    new_transaction.setVisibility(View.VISIBLE);
                    transactionsAdapter.swapData(ts);
                    transactionsAdapter.notifyDataSetChanged();
                    cashAccountsAdapter.swapData(cas);
                    cashAccountsAdapter.notifyDataSetChanged();
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
                    balances.setVisibility(View.GONE);
                    balancesAdapter.swapData(null);
                    if(balance.count() == 0 && balance.minorCount() == 0)
                    {
                        balance_value.setText(nothing_label);
                        balance_value.setTextColor(neutralColor);
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
                    balance_value.setTextColor(balance.income() ? positiveColor : negativeColor);
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
                    balances.setVisibility(View.VISIBLE);
                    balancesAdapter.swapData(balance);
                    balancesAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    private MainContract.Behaviour behaviour;
    private final CashAccountsAdapter.Listener cashAccountsAdapterListener = new CashAccountsAdapter.Listener()
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
    };
    private final TransactionsAdapter.Listener transactionsAdapterListener = new TransactionsAdapter.Listener()
    {
        public void delete(Transaction transaction)
        {
            deleteTransaction(transaction);
        }
    };

    private View cash_accounts_container;
    private View empty_cash_accounts;
    private View empty_transactions;
    private TextView balance_text;
    private RecyclerView cash_accounts;
    private RecyclerView transactions;
    private View new_transaction;
    private TextView balance_value;
    private Spinner balances;

    private TransactionsAdapter transactionsAdapter;
    private CashAccountsAdapter cashAccountsAdapter;
    private BalancesAdapter balancesAdapter;
    private String balance_label;
    private String nothing_label;
    private int positiveColor;
    private int neutralColor;
    private int negativeColor;

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
            case R.id.logout:
                behaviour.logout();
                break;
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
        cash_accounts_container = findView(R.id.cash_accounts_container);
        empty_cash_accounts = findView(R.id.empty_cash_accounts);
        empty_transactions = findView(R.id.empty_transactions);
        balance_text = findView(R.id.balance_text);
        cash_accounts = findView(R.id.cash_accounts);
        transactions = findView(R.id.transactions);
        new_transaction = findView(R.id.new_transaction);
        balance_value = findView(R.id.balance_value);
        balances = findView(R.id.balances);
        setClickListener(findView(R.id.logout), new_transaction, findView(R.id.add_new_cash_account));
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
        transactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionsAdapter = new TransactionsAdapter(getActivity(), transactionsAdapterListener);
        transactions.setAdapter(transactionsAdapter);
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(getActivity());
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cash_accounts.setLayoutManager(cashAccountsLinearLayoutManager);
        cashAccountsAdapter = new CashAccountsAdapter(getActivity(), cashAccountsAdapterListener);
        cash_accounts.setAdapter(cashAccountsAdapter);
        balancesAdapter = new BalancesAdapter(getActivity());
        balances.setAdapter(balancesAdapter);
        balance_label = getActivity().getResources().getString(R.string.balance_label);
        nothing_label = getActivity().getResources().getString(R.string.nothing_label);
        positiveColor = getActivity().getResources().getColor(R.color.green);
        neutralColor = getActivity().getResources().getColor(R.color.black);
        negativeColor = getActivity().getResources().getColor(R.color.red);
        cash_accounts_container.setVisibility(View.GONE);
        empty_cash_accounts.setVisibility(View.GONE);
        empty_transactions.setVisibility(View.GONE);
        transactions.setVisibility(View.GONE);
        new_transaction.setVisibility(View.GONE);
        balance_text.setVisibility(View.GONE);
        balance_value.setVisibility(View.GONE);
        balances.setVisibility(View.GONE);
        presenter.update();
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