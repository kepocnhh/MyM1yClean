package stan.mym1y.clean.modules.cashaccounts;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.contracts.cashaccounts.AddNewCashAccountContract;
import stan.mym1y.clean.cores.cashaccounts.CashAccountViewModel;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.modules.cashaccounts.currencies.CurrenciesList;
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
                    currenciesList.swapData(currencies);
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

    private View background;
    private EditText title;
    private TextView enter_title_text;
    private TextView set_currency_text;
    private TextView add;
    private TextView cancel;

    private CurrenciesList currenciesList;
    private Theme currentTheme;

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
        background = findView(R.id.background);
        title = findView(R.id.title);
        enter_title_text = findView(R.id.enter_title_text);
        set_currency_text = findView(R.id.set_currency_text);
        add = findView(R.id.add);
        cancel = findView(R.id.cancel);
        setClickListener(findView(R.id.add), findView(R.id.cancel));
    }
    protected void init()
    {
        currenciesList = new CurrenciesList(getActivity(), (RecyclerView)findView(R.id.currencies), App.component().themeSwitcher().theme(), new CurrenciesList.Listener()
        {
            public void currency(Currency currency)
            {
                presenter.setCurrency(currency);
            }
        });
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
        setTheme(App.component().themeSwitcher().theme());
        presenter = new AddNewCashAccountPresenter(view, new AddNewCashAccountModel(App.component().dataLocal().currenciesAccess().currencies()));
        presenter.update();
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        background.setBackgroundColor(currentTheme.colors().background());
        title.setTextColor(currentTheme.colors().foreground());
        enter_title_text.setTextColor(currentTheme.colors().foreground());
        set_currency_text.setTextColor(currentTheme.colors().foreground());
        add.setTextColor(currentTheme.colors().accent());
        cancel.setTextColor(currentTheme.colors().foreground());
    }
}