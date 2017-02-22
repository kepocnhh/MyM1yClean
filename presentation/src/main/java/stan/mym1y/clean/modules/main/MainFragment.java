package stan.mym1y.clean.modules.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.units.fragments.MVPFragment;

public class MainFragment
        extends MVPFragment<MainContract.Presenter>
{
    static public MVPFragment newInstanse(MainContract.Behaviour b)
    {
        MainFragment fragment = new MainFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private TextView balance;
    private RecyclerView transactions;

    private TransactionsAdapter adapter;

    private final MainContract.View view = new MainContract.View()
    {
        @Override
        public void error(ErrorsContract.NetworkErrorException exception)
        {
            showToast("NetworkErrorException");
        }
        @Override
        public void error(ErrorsContract.UnauthorizedException exception)
        {
            showToast("UnauthorizedException");
        }
        @Override
        public void error(ErrorsContract.InvalidDataException exception)
        {
//            showToast("InvalidDataException");
            behaviour.logout();
        }
        @Override
        public void error(ErrorsContract.ServerErrorException exception)
        {
            showToast("ServerErrorException");
        }
        @Override
        public void error(ErrorsContract.UnknownErrorException exception)
        {
            showToast("UnknownErrorException");
        }
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
                    balance.setText(balance_label + ": " + blnc);
                }
            });
        }
    };
    private MainContract.Behaviour behaviour;
    private final TransactionsAdapterListener transactionsAdapterListener = new TransactionsAdapterListener()
    {
        @Override
        public void delete(int id)
        {
//            deleteTransaction(id);
        }
    };

    private String balance_label;

    @Override
    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.logout:
                behaviour.logout();
                break;
        }
    }

    @Override
    protected int getContentView()
    {
        return R.layout.main_screen;
    }
    @Override
    protected void initViews(View v)
    {
        balance = findView(R.id.balance);
        transactions = findView(R.id.transactions);
        setClickListener(findView(R.id.logout));
    }
    @Override
    protected void init()
    {
        setPresenter(new MainPresenter(view, new MainModel(
                App.getAppComponent().getDataAccess().getTransactions()
                ,App.getAppComponent().getConnection()
                ,App.getAppComponent().getSettings()
        )));
        transactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TransactionsAdapter(getActivity(), transactionsAdapterListener);
        transactions.setAdapter(adapter);
        balance_label = getActivity().getResources().getString(R.string.balance_label);
        getPresenter().update();
    }
}