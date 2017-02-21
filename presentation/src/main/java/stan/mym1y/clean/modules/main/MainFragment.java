package stan.mym1y.clean.modules.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.ErrorsContract;
import stan.mym1y.clean.contracts.MainContract;
import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.cores.users.UserPrivateData;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.modules.users.UserData;
import stan.mym1y.clean.units.fragments.MVPFragment;

public class MainFragment
        extends MVPFragment<MainContract.Presenter>
{
    static private final String USER_ID = "user_id";
    static private final String USER_TOKEN = "user_token";

    static public MVPFragment newInstanse(MainContract.Behaviour b, UserPrivateData userPrivateData)
    {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userPrivateData.getUserId());
        bundle.putString(USER_TOKEN, userPrivateData.getUserToken());
        fragment.setArguments(bundle);
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
            showToast("InvalidDataException");
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
        public void update(int balance)
        {
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
                ,new UserData(getArguments().getString(USER_ID), getArguments().getString(USER_TOKEN))
        )));
        transactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TransactionsAdapter(getActivity(), transactionsAdapterListener);
        transactions.setAdapter(adapter);
        getPresenter().update();
    }
}