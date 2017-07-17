package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.units.adapters.Holder;

class CashAccountHolder
        extends Holder
{
    private final TextView title;
    private final TextView balance;
    private final TextView currency;

    private final String nothing_label;
    private final int positiveColor;
    private final int neutralColor;
    private final int negativeColor;

    CashAccountHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.cashaccount_list_item);
        title = view(R.id.title);
        balance = view(R.id.balance);
        currency = view(R.id.currency);
        nothing_label = context.getResources().getString(R.string.nothing_label);
        positiveColor = context.getResources().getColor(R.color.green);
        neutralColor = context.getResources().getColor(R.color.black);
        negativeColor = context.getResources().getColor(R.color.red);
    }

    void setClick(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }
    void setLongClick(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
    }
    void render(CashAccount cashAccount, CashAccount.Extra extra)
    {
//        id.setText(""+cashAccount.id());
        title.setText(""+cashAccount.title());
        currency.setText(extra.currency().codeName());
        if(extra.count() == 0 && extra.minorCount() == 0)
        {
            balance.setText(nothing_label);
            balance.setTextColor(neutralColor);
            return;
        }
        String left = extra.income() ? "+" : "-";
        String middle = String.valueOf(Math.abs(extra.count()));
        switch(extra.currency().minorUnitType())
        {
            case TEN:
                middle += "." + String.valueOf(extra.minorCount());
                break;
            case HUNDRED:
                middle += "." + (extra.minorCount() < 10 ? "0" + extra.minorCount() : extra.minorCount());
                break;
        }
        balance.setText(left + middle);
        balance.setTextColor(extra.income() ? positiveColor : negativeColor);
    }
}