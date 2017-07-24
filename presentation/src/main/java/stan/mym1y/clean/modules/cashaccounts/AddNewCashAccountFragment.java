package stan.mym1y.clean.modules.cashaccounts;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import stan.mym1y.clean.utils.ValueAnimator;

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
    private View title_bottom;
    private TextView enter_title_text;
    private TextView set_currency_text;
    private TextView add;
    private TextView cancel;

    private CurrenciesList currenciesList;
    private Theme currentTheme;
    private ValueAnimator.Animator enterTitleTextAnimator;
    private ValueAnimator.Animator titleBottomAnimator;
    private ValueAnimator.Animator beginAnimator;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.add:
                presenter.addNewCashAccount();
                break;
            case R.id.cancel:
                tryCancel();
                break;
        }
    }
    private void tryCancel()
    {
        animate(false, new ValueAnimator.AnimationListener()
        {
            public void begin()
            {
            }
            public void end()
            {
                behaviour.cancel();
            }
            public void cancel()
            {
            }
        });
    }
    protected boolean onBackPressed()
    {
        tryCancel();
        return true;
    }
    protected int getContentView()
    {
        return R.layout.add_new_cash_account_screen;
    }
    protected void initViews(View v)
    {
        background = findView(R.id.background);
        title = findView(R.id.title);
        title_bottom = findView(R.id.title_bottom);
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
                title.clearFocus();
                hideKeyBoard();
            }
        });
//        title.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        title.setOnEditorActionListener(new TextView.OnEditorActionListener()
//        {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
//            {
//                switch(actionId)
//                {
//                    case EditorInfo.IME_ACTION_DONE:
//                        mainView().requestFocus();
//                        break;
//                }
//                return false;
//            }
//        });
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
        title.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            public void onFocusChange(View v, boolean hasFocus)
            {
//                log("title_bottom focus: " + hasFocus);
                title_bottom.setBackgroundColor(hasFocus ? currentTheme.colors().accent() : currentTheme.colors().foreground());
//                if(!hasFocus)
//                {
//                    hideKeyBoard();
//                    mainView().requestFocus();
//                }
                if(title.getText().length() == 0 && hasFocus)
                {
                    animate(true);
                }
                else if(title.getText().length() == 0 && !hasFocus)
                {
                    animate(false);
                }
            }
        });
        setTheme(App.component().themeSwitcher().theme());
        background.setVisibility(View.INVISIBLE);
        animate(true, new ValueAnimator.AnimationListener()
        {
            public void begin()
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        background.setAlpha(0);
                        background.setVisibility(View.VISIBLE);
                    }
                });
            }
            public void end()
            {
            }
            public void cancel()
            {
            }
        });
        title_bottom.setVisibility(View.INVISIBLE);
        title.post(new Runnable()
        {
            public void run()
            {
                float width = mainView().getWidth() - px(12)*2;
                title_bottom.setX(mainView().getWidth()/2 - width/2);
                title_bottom.getLayoutParams().width = (int)width;
                title_bottom.setLayoutParams(title_bottom.getLayoutParams());
                title_bottom.setVisibility(View.VISIBLE);
                animate(false);
            }
        });
        presenter = new AddNewCashAccountPresenter(view, new AddNewCashAccountModel(App.component().dataLocal().currenciesAccess().currencies()));
        presenter.update();
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        background.setBackgroundColor(currentTheme.colors().background());
        title.setTextColor(currentTheme.colors().foreground());
        title_bottom.setBackgroundColor(currentTheme.colors().foreground());
        enter_title_text.setTextColor(currentTheme.colors().foreground());
        set_currency_text.setTextColor(currentTheme.colors().foreground());
        add.setTextColor(currentTheme.colors().accent());
        cancel.setTextColor(currentTheme.colors().foreground());
    }

    private void animate(final boolean open)
    {
        int maxTime = 250;
        float state = enter_title_text.getY() / title.getY();
        animateEnterTitleText(state, open ? 0 : 1, open ? (int)(maxTime*state) : maxTime - (int)(maxTime*state));
        state = title_bottom.getWidth() / (mainView().getWidth() - px(12)*2);
        animateTitleBottom(state, open ? 1 : 0, open ? maxTime - (int)(maxTime*state) : (int)(maxTime*state));
    }
    private void animateTitleBottom(float from, float to, int time)
    {
        if(titleBottomAnimator != null)
        {
            titleBottomAnimator.cancel();
        }
        titleBottomAnimator = ValueAnimator.create(time, from, to, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        float width = (mainView().getWidth() - px(12)*2)/2 + ((mainView().getWidth() - px(12)*2)/2)*value;
                        title_bottom.setX(mainView().getWidth()/2 - width/2);
                        title_bottom.getLayoutParams().width = (int)width;
                        title_bottom.setLayoutParams(title_bottom.getLayoutParams());
                    }
                });
            }
        });
        titleBottomAnimator.animate();
    }
    private void animateEnterTitleText(float from, float to, int time)
    {
        if(enterTitleTextAnimator != null)
        {
            enterTitleTextAnimator.cancel();
        }
        enterTitleTextAnimator = ValueAnimator.create(time, from, to, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        enter_title_text.setY(title.getY()*value);
//                        enter_title_text.setX(px(12)*value);
                        enter_title_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, px(14) + px(8)*value);
                    }
                });
            }
        });
        enterTitleTextAnimator.animate();
    }
    private void animate(boolean in, ValueAnimator.AnimationListener listener)
    {
        if(beginAnimator != null)
        {
            beginAnimator.cancel();
        }
        beginAnimator = ValueAnimator.create(250, in ? 0 : 1, in ? 1 : 0, new ValueAnimator.Updater<Float>()
        {
            public void update(final Float value)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        background.setAlpha(value);
                        int pow = 2;
//                        set_cash_account_container.setX(mainView().getWidth() - mainView().getWidth()*value);
//                        enter_count_container.setX(mainView().getWidth() - mainView().getWidth()*pow(value, pow));
//                        buttons_bottom_container.setX(mainView().getWidth() - mainView().getWidth()*value);
                    }
                });
            }
        });
        beginAnimator.setAnimationListener(listener);
        beginAnimator.animate();
    }
}