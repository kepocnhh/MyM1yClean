package stan.mym1y.clean.modules.transactions;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.transactions.AddNewTransactionContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.transactions.TransactionViewModel;
import stan.mym1y.clean.modules.transactions.cashaccounts.CashAccountsAdapter;
import stan.mym1y.clean.units.fragments.UtilFragment;

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
                    cashAccountsAdapter.swapData(cashAccounts);
                    cashAccountsAdapter.notifyDataSetChanged();
                }
            });
        }
        public void addNewCashAccount(TransactionViewModel transactionViewModel)
        {
            behaviour.newTransaction(transactionViewModel);
        }
        public void error(AddNewTransactionContract.ValidateDataException exception)
        {
            toast("Count must be not zero!");
        }
    };
    private AddNewTransactionContract.Behaviour behaviour;

    private RecyclerView cash_accounts;
    private TextView count_text;

    private CashAccountsAdapter cashAccountsAdapter;
    private final EnterCountFragment.Listener enterCountListener = new EnterCountFragment.Listener()
    {
        public void confirm(int count, int minorCount)
        {
            presenter.setCount(count, minorCount);
            count_text.setText("" + count + "." + minorCount);
            clear(R.id.enter_count_subscreen);
            setSystemUiVisibilityLight(true);
            setStatusBarColor(getActivity().getResources().getColor(R.color.white));
        }
        public void cancel()
        {
            clear(R.id.enter_count_subscreen);
            setSystemUiVisibilityLight(true);
            setStatusBarColor(getActivity().getResources().getColor(R.color.white));
        }
    };

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
                replace(R.id.enter_count_subscreen, EnterCountFragment.newInstance(enterCountListener, 0, 0));
                setSystemUiVisibilityLight(false);
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.add_new_transaction_screen;
    }
    protected void initViews(View v)
    {
        cash_accounts = findView(R.id.cash_accounts);
        count_text = findView(R.id.count_text);
        setClickListener(findView(R.id.add), findView(R.id.cancel), count_text);
    }
    protected void init()
    {
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(getActivity());
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cash_accounts.setLayoutManager(cashAccountsLinearLayoutManager);
        cashAccountsAdapter = new CashAccountsAdapter(getActivity(), new CashAccountsAdapter.Listener()
        {
            public void cashAccount(CashAccount cashAccount)
            {
                presenter.setCashAccount(cashAccount);
            }
        });
        cash_accounts.setAdapter(cashAccountsAdapter);
        presenter = new AddNewTransactionPresenter(view, new AddNewTransactionModel(App.component().dataLocal().cashAccountsAccess().cashAccounts()));
        presenter.update();
        presenter.setDate(System.currentTimeMillis());
        count_text.setText("0.0");
    }
}