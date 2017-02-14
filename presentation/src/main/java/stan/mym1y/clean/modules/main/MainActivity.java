package stan.mym1y.clean.modules.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.modules.transaction.AddNewTransactionDialog;
import stan.mym1y.clean.modules.transactions.Transaction;

public class MainActivity
        extends Activity
{
    private TextView balance;
    private RecyclerView transactions;

    private TransactionsAdapter adapter;

    private final View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch(view.getId())
            {
                case R.id.new_transaction:
                    newTransaction();
                    break;
            }
        }
    };

    private MainContract.Presenter presenter;
    private final MainContract.View view = new MainContract.View()
    {
        @Override
        public void update(final ListModel<TransactionModel> transactions)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    adapter.swapData(transactions);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        @Override
        public void update(final int blnc)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    balance.setText("Balance: " + blnc);
                }
            });
        }
    };
    private final AddNewTransactionDialog.AddNewTransactionListener addNewTransactionListener = new AddNewTransactionDialog.AddNewTransactionListener()
    {
        @Override
        public void newTransaction(int count)
        {
            presenter.newTransaction(new Transaction(-1, count, System.currentTimeMillis()));
        }
    };

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.main_activity);
        initViews();
        init();
    }
    private void initViews()
    {
        balance = findView(R.id.balance);
        transactions = findView(R.id.transactions);
        setClickListener(findView(R.id.new_transaction));
    }
    private void init()
    {
        presenter = new MainPresenter(view, new MainModel(App.getAppComponent().getDataAccess().getTransactions()));
        transactions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionsAdapter(this);
        transactions.setAdapter(adapter);
        presenter.update();
    }

    private void newTransaction()
    {
        AddNewTransactionDialog.newInstanse(addNewTransactionListener)
                               .show(getFragmentManager(), AddNewTransactionDialog.class.getName());
    }

    private <VIEW extends View> VIEW findView(int id)
    {
        return (VIEW)findViewById(id);
    }
    private void setClickListener(View... views)
    {
        for(View v : views)
        {
            if(v != null)
            {
                v.setOnClickListener(clickListener);
            }
        }
    }
}