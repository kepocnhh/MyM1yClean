package stan.mym1y.clean.modules.transactions;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class EnterCountFragment
    extends UtilFragment
{
    static private String MINOR_UNIT_TYPE = "minor_unit_type";
    static private String COUNT = "count";
    static private String MINOR_COUNT = "minor_count";
    static private String INCOME = "income";
    static public UtilFragment newInstance(Listener l, Currency.MinorUnitType minorUnitType, boolean income, int count, int minorCount)
    {
        EnterCountFragment fragment = new EnterCountFragment();
        fragment.listener = l;
        Bundle bundle = new Bundle();
        bundle.putString(MINOR_UNIT_TYPE, minorUnitType.name());
        bundle.putBoolean(INCOME, income);
        bundle.putInt(COUNT, count);
        bundle.putInt(MINOR_COUNT, minorCount);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Currency.MinorUnitType minorUnitType;
    private int count = 0;
    private int minorCount = 0;
    private boolean incomeState;
    private boolean toMinor;

    private View background;
    private TextView income;
    private TextView outcome;
    private TextView count_value;

    private TextView value_0;
    private TextView value_1;
    private TextView value_2;
    private TextView value_3;
    private TextView value_4;
    private TextView value_5;
    private TextView value_6;
    private TextView value_7;
    private TextView value_8;
    private TextView value_9;
    private TextView to_minor;
    private ImageView backspace;
    private ImageView cancel;
    private ImageView confirm;


    private Listener listener;
    private Theme currentTheme;

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.value_1:
                updateCount(1);
                break;
            case R.id.value_2:
                updateCount(2);
                break;
            case R.id.value_3:
                updateCount(3);
                break;
            case R.id.value_4:
                updateCount(4);
                break;
            case R.id.value_5:
                updateCount(5);
                break;
            case R.id.value_6:
                updateCount(6);
                break;
            case R.id.value_7:
                updateCount(7);
                break;
            case R.id.value_8:
                updateCount(8);
                break;
            case R.id.value_9:
                updateCount(9);
                break;
            case R.id.value_0:
                updateCount(0);
                break;
            case R.id.to_minor:
                if(minorUnitType != Currency.MinorUnitType.NONE && !toMinor)
                {
                    toMinor();
                }
                break;
            case R.id.backspace:
                backspace();
                break;
            case R.id.confirm:
                listener.confirm(incomeState, count, minorCount);
                break;
            case R.id.cancel:
                listener.cancel();
                break;
            case R.id.income:
                if(!incomeState)
                {
                    incomeState = true;
                    updateCountColor();
                    updateCountText();
                }
                break;
            case R.id.outcome:
                if(incomeState)
                {
                    incomeState = false;
                    updateCountColor();
                    updateCountText();
                }
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.enter_count_screen;
    }
    protected void initViews(View v)
    {
        background = findView(R.id.background);
        income = findView(R.id.income);
        outcome = findView(R.id.outcome);
        count_value = findView(R.id.count_value);
        cancel = findView(R.id.cancel);
        confirm = findView(R.id.confirm);
        initKeyboard();
        setClickListener(value_0,
                value_1, value_2, value_3,
                value_4, value_5, value_6,
                value_7, value_8, value_9,
                to_minor,
                backspace,
                outcome,
                income,
                cancel,
                confirm);
    }
    private void initKeyboard()
    {
        value_0 = findView(R.id.value_0);
        value_1 = findView(R.id.value_1);
        value_2 = findView(R.id.value_2);
        value_3 = findView(R.id.value_3);
        value_4 = findView(R.id.value_4);
        value_5 = findView(R.id.value_5);
        value_6 = findView(R.id.value_6);
        value_7 = findView(R.id.value_7);
        value_8 = findView(R.id.value_8);
        value_9 = findView(R.id.value_9);
        to_minor = findView(R.id.to_minor);
        backspace = findView(R.id.backspace);
    }
    protected void init()
    {
        minorUnitType = Currency.MinorUnitType.valueOf(getArguments().getString(MINOR_UNIT_TYPE));
        count = getArguments().getInt(COUNT);
        minorCount = getArguments().getInt(MINOR_COUNT);
        incomeState = getArguments().getBoolean(INCOME);
        if(count < 0)
        {
            count *= -1;
        }
        to_minor.setVisibility(minorUnitType == Currency.MinorUnitType.NONE ? View.INVISIBLE : View.VISIBLE);
        toMinor = false;
        setTheme(App.component().themeSwitcher().theme());
        updateCountColor();
        updateCountText();
    }
    private void setTheme(Theme theme)
    {
        currentTheme = theme;
        background.setBackgroundColor(currentTheme.colors().background());
        income.setTextColor(currentTheme.colors().positive());
        outcome.setTextColor(currentTheme.colors().negative());
        setForeground(currentTheme.colors().foreground());
    }
    private void setForeground(int foregroundColor)
    {
        value_0.setTextColor(foregroundColor);
        value_1.setTextColor(foregroundColor);
        value_2.setTextColor(foregroundColor);
        value_3.setTextColor(foregroundColor);
        value_4.setTextColor(foregroundColor);
        value_5.setTextColor(foregroundColor);
        value_6.setTextColor(foregroundColor);
        value_7.setTextColor(foregroundColor);
        value_8.setTextColor(foregroundColor);
        value_9.setTextColor(foregroundColor);
        to_minor.setTextColor(foregroundColor);
        backspace.setColorFilter(foregroundColor);
        confirm.setColorFilter(foregroundColor);
        cancel.setColorFilter(foregroundColor);
    }

    private void updateCount(int c)
    {
        if(toMinor)
        {
            if(minorCount == 0 && c == 0)
            {
                return;
            }
            switch(minorUnitType)
            {
                case TEN:
                    if(minorCount > 0)
                    {
                        return;
                    }
                    break;
                case HUNDRED:
                    if(minorCount > 9)
                    {
                        return;
                    }
                    break;
            }
            if(c == 0)
            {
                minorCount *= 10;
                updateCountText();
            }
            else
            {
                minorCount = minorCount*10 + c;
                updateCountText();
            }
        }
        else
        {
            if(count == 0 && c == 0)
            {
                return;
            }
            if(count < 1_000_000)
            {
                count = count*10 + c;
                updateCountText();
            }
        }
    }
    private void updateCountText()
    {
        switch(minorUnitType)
        {
            case NONE:
                count_value.setText((incomeState ? "+" : "-") + count);
                break;
            case TEN:
                count_value.setText((incomeState ? "+" : "-") + count + "." + minorCount);
                break;
            case HUNDRED:
                count_value.setText((incomeState ? "+" : "-") + count + "." + (minorCount < 10 ? "0" + minorCount : minorCount));
                break;
        }
    }
    private void updateCountColor()
    {
        count_value.setBackgroundColor(incomeState ? currentTheme.colors().positive() : currentTheme.colors().negative());
        setStatusBarColor(incomeState ? currentTheme.colors().positive() : currentTheme.colors().negative());
    }
    private void toMinor()
    {
        if(!toMinor)
        {
            toMinor = true;
        }
    }
    private void backspace()
    {
        if(toMinor)
        {
            if(minorCount == 0)
            {
                toMinor = false;
                count = count/10;
            }
            else
            {
                minorCount = minorCount/10;
            }
            updateCountText();
        }
        else
        {
            count = count/10;
            updateCountText();
        }
    }

    public interface Listener
    {
        void confirm(boolean income, int count, int minorCount);
        void cancel();
    }
}