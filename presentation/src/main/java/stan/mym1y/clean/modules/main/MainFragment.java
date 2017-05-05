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
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.modules.transaction.AddNewTransactionDialog;
import stan.mym1y.clean.modules.transaction.DeleteTransactionConfirmDialog;
import stan.mym1y.clean.modules.transactions.TransactionView;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class MainFragment
        extends UtilFragment
{
    static public UtilFragment newInstanse(MainContract.Behaviour b)
    {
        MainFragment fragment = new MainFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private TextView balance;
    private RecyclerView transactions;

    private TransactionsAdapter adapter;

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
        public void update(final List<Transaction> transactions)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    adapter.swapData(transactions);
                    adapter.notifyDataSetChanged();
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
    private final TransactionsAdapterListener transactionsAdapterListener = new TransactionsAdapterListener()
    {
        public void delete(int id)
        {
            deleteTransaction(id);
        }
    };

    private String balance_label;

    private final AddNewTransactionDialog.AddNewTransactionListener addNewTransactionListener = new AddNewTransactionDialog.AddNewTransactionListener()
    {
        public void newTransaction(int count)
        {
            presenter.newTransaction(new TransactionView(count, System.currentTimeMillis()));
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
        }
    }
    protected int getContentView()
    {
        return R.layout.main_screen;
    }
    protected void initViews(View v)
    {
        balance = findView(R.id.balance);
        transactions = findView(R.id.transactions);
        setClickListener(findView(R.id.logout), findView(R.id.new_transaction));
    }
    protected void init()
    {
        presenter = new MainPresenter(view, new MainModel(App.component().dataLocal().transactionsAccess().transactions(),
                App.component().security(),
                App.component().settings(),
                App.component().jsonConverter(),
                App.component().dataRemote().authApi(),
                App.component().dataRemote().dataApi()));
        transactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TransactionsAdapter(getActivity(), transactionsAdapterListener);
        transactions.setAdapter(adapter);
        balance_label = getActivity().getResources().getString(R.string.balance_label);
        presenter.update();
    }

    private void newTransaction()
    {
        AddNewTransactionDialog.newInstanse(addNewTransactionListener).show(getFragmentManager(), AddNewTransactionDialog.class.getName());
    }
    private void deleteTransaction(final int id)
    {
        DeleteTransactionConfirmDialog.newInstanse(new DeleteTransactionConfirmDialog.DeleteTransactionConfirmListener()
        {
            public void confirm()
            {
                presenter.deleteTransaction(id);
            }
        }).show(getFragmentManager(), DeleteTransactionConfirmDialog.class.getName());
    }
}