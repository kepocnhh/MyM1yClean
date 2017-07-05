package stan.mym1y.clean.modules.transactions;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.units.fragments.UtilFragment;

public class EnterCountFragment
    extends UtilFragment
{
    static private String COUNT = "count";
    static private String MINOR_COUNT = "minor_count";
    static public UtilFragment newInstance(Listener l, int count, int minorCount)
    {
        EnterCountFragment fragment = new EnterCountFragment();
        fragment.listener = l;
        Bundle bundle = new Bundle();
        bundle.putInt(COUNT, count);
        bundle.putInt(MINOR_COUNT, minorCount);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int count = 0;
    private int minorCount = 0;
    private boolean income;

    private TextView count_value;

    private Listener listener;
    private int positiveColor;
    private int negativeColor;

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
            case R.id.backspace:
                count = count/10;
                if(count == 0)
                {
                    if(!income)
                    {
                        income = true;
                        updateCountColor();
                    }
                }
                updateCountText();
                break;
            case R.id.confirm:
                listener.confirm(count, minorCount);
                break;
            case R.id.cancel:
                listener.cancel();
                break;
            case R.id.income:
                if(!income)
                {
                    income = true;
                    updateCountColor();
                    updateCountText();
                }
                break;
            case R.id.outcome:
                if(income)
                {
                    income = false;
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
        count_value = findView(R.id.count_value);
        setClickListener(findView(R.id.value_0),
                findView(R.id.value_1),
                findView(R.id.value_2),
                findView(R.id.value_3),
                findView(R.id.value_4),
                findView(R.id.value_5),
                findView(R.id.value_6),
                findView(R.id.value_7),
                findView(R.id.value_8),
                findView(R.id.value_9),
                findView(R.id.to_minor),
                findView(R.id.backspace),
                findView(R.id.outcome),
                findView(R.id.income),
                findView(R.id.backspace),
                findView(R.id.cancel),
                findView(R.id.confirm));
    }
    protected void init()
    {
        positiveColor = getActivity().getResources().getColor(R.color.green);
        negativeColor = getActivity().getResources().getColor(R.color.red);
        count = getArguments().getInt(COUNT);
        minorCount = getArguments().getInt(MINOR_COUNT);
        income = count >= 0;
        if(count < 0)
        {
            count *= -1;
        }
        updateCountColor();
        updateCountText();
    }

    private void updateCount(int c)
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
    private void updateCountText()
    {
        count_value.setText((income ? "+" : "-") + count + "." + minorCount);
    }
    private void updateCountColor()
    {
        count_value.setBackgroundColor(income ? positiveColor : negativeColor);
        setStatusBarColor(income ? positiveColor : negativeColor);
    }

    public interface Listener
    {
        void confirm(int count, int minorCount);
        void cancel();
    }
}