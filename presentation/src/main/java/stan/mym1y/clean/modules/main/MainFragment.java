package stan.mym1y.clean.modules.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.modules.cashaccounts.AddNewCashAccountDialog;
import stan.mym1y.clean.modules.cashaccounts.CashAccountView;
import stan.mym1y.clean.modules.main.cashaccounts.CashAccountsAdapter;
import stan.mym1y.clean.modules.main.transactions.TransactionsAdapter;
import stan.mym1y.clean.modules.transactions.AddNewTransactionDialog;
import stan.mym1y.clean.modules.transactions.DeleteTransactionConfirmDialog;
import stan.mym1y.clean.modules.transactions.TransactionView;
import stan.mym1y.clean.units.fragments.UtilFragment;

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
                }
            });
        }
        public void emptyTransactions(final List<CashAccount> cashAccounts)
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
                }
            });
        }
        public void update(final List<CashAccount> cas, final List<Transaction> ts)
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
        public void update(final int b)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    balance.setText(balance_label + ": " + b);
                }
            });
        }
    };
    private MainContract.Behaviour behaviour;
    private final CashAccountsAdapter.Listener cashAccountsAdapterListener = new CashAccountsAdapter.Listener()
    {
        public void delete(CashAccount cashAccount)
        {
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
    private TextView balance;
    private RecyclerView cash_accounts;
    private RecyclerView transactions;
    private View new_transaction;

    private TransactionsAdapter transactionsAdapter;
    private CashAccountsAdapter cashAccountsAdapter;
    private String balance_label;

    private final AddNewTransactionDialog.Listener addNewTransactionListener = new AddNewTransactionDialog.Listener()
    {
        public void newTransaction(int count)
        {
            presenter.add(new TransactionView(-1, System.currentTimeMillis(), count));//TODO create cash account system
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
        balance = findView(R.id.balance);
        cash_accounts = findView(R.id.cash_accounts);
        transactions = findView(R.id.transactions);
        new_transaction = findView(R.id.new_transaction);
        setClickListener(findView(R.id.logout), new_transaction, findView(R.id.add_new_cash_account));
    }
    protected void init()
    {
        presenter = new MainPresenter(view, new MainModel(App.component().dataLocal().transactionsAccess().transactions(),
                App.component().dataLocal().cashAccountsAccess().cashAccounts(),
                App.component().security(),
                App.component().settings(),
                App.component().jsonConverter(),
                App.component().dataRemote().authApi(),
                App.component().dataRemote().dataApi()));
        transactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionsAdapter = new TransactionsAdapter(getActivity(), transactionsAdapterListener);
        transactions.setAdapter(transactionsAdapter);
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(getActivity());
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cash_accounts.setLayoutManager(cashAccountsLinearLayoutManager);
        cashAccountsAdapter = new CashAccountsAdapter(getActivity(), cashAccountsAdapterListener);
        cash_accounts.setAdapter(cashAccountsAdapter);
        balance_label = getActivity().getResources().getString(R.string.balance_label);
        presenter.update();
        cash_accounts_container.setVisibility(View.GONE);
        empty_cash_accounts.setVisibility(View.GONE);
        empty_transactions.setVisibility(View.GONE);
        transactions.setVisibility(View.GONE);
        new_transaction.setVisibility(View.GONE);
    }

    private void newCashAccount()
    {
        AddNewCashAccountDialog.newInstance(new AddNewCashAccountDialog.Listener()
        {
            public void newCashAccount(String title)
            {
                presenter.add(new CashAccountView(title));
            }
        }).show(getFragmentManager(), AddNewCashAccountDialog.class.getName());
    }

    private void newTransaction()
    {
        AddNewTransactionDialog.newInstance(addNewTransactionListener).show(getFragmentManager(), AddNewTransactionDialog.class.getName());
    }
    private void deleteTransaction(final Transaction transaction)
    {
        DeleteTransactionConfirmDialog.newInstanse(new DeleteTransactionConfirmDialog.Listener()
        {
            public void confirm()
            {
                presenter.delete(transaction);
            }
        }).show(getFragmentManager(), DeleteTransactionConfirmDialog.class.getName());
    }
}