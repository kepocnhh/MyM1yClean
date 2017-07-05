package stan.mym1y.clean.modules.cashaccounts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.cashaccounts.AddNewCashAccountContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.modules.cashaccounts.currencies.CurrenciesAdapter;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class AddNewCashAccountFragment
    extends UtilFragment
{
    static public UtilFragment newInstance(AddNewCashAccountContract.Behaviour b)
    {
        AddNewCashAccountFragment fragment = new AddNewCashAccountFragment();
        fragment.behaviour = b;
        return fragment;
    }

    private AddNewCashAccountContract.Presenter presenter;
    private AddNewCashAccountContract.View view = new AddNewCashAccountContract.View()
    {
        public void currencies(final List<Currency> currencies)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    currenciesAdapter.swapData(currencies);
                    currenciesAdapter.notifyDataSetChanged();
                }
            });
        }
        public void addNewCashAccount(CashAccountViewModel cashAccountViewModel)
        {
            behaviour.newCashAccount(cashAccountViewModel);
        }
        public void error(AddNewCashAccountContract.ValidateDataException exception)
        {
            toast("Title must be not empty!");
        }
    };
    private AddNewCashAccountContract.Behaviour behaviour;

    private EditText title;
    private RecyclerView currencies;

    private CurrenciesAdapter currenciesAdapter;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.add:
                presenter.addNewCashAccount();
                break;
            case R.id.cancel:
                behaviour.cancel();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.add_new_cash_account_screen;
    }
    protected void initViews(View v)
    {
        title = findView(R.id.title);
        currencies = findView(R.id.currencies);
        setClickListener(findView(R.id.add), findView(R.id.cancel));
    }
    protected void init()
    {
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(getActivity());
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        currencies.setLayoutManager(cashAccountsLinearLayoutManager);
        currenciesAdapter = new CurrenciesAdapter(getActivity(), new CurrenciesAdapter.Listener()
        {
            public void currency(Currency currency)
            {
                presenter.setCurrency(currency);
            }
        });
        currencies.setAdapter(currenciesAdapter);
        title.addTextChangedListener(new TextWatcher()
        {
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                presenter.setTitle(s.toString());
            }
            public void afterTextChanged(Editable s)
            {
            }
        });
        presenter = new AddNewCashAccountPresenter(view, new AddNewCashAccountModel(App.component().dataLocal().currenciesAccess().currencies()));
        presenter.update();
    }
}