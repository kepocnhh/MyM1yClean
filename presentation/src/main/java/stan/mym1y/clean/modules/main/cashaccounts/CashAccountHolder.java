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
//    private final TextView id;
    private final TextView title;
    private final TextView balance;

    private final int positiveColor;
    private final int neutralColor;
    private final int negativeColor;

    CashAccountHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.cashaccount_list_item);
//        id = view(R.id.id);
        title = view(R.id.title);
        balance = view(R.id.balance);
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
        if(extra.balance() < 0)
        {
            balance.setText(String.valueOf(extra.balance()));
            balance.setTextColor(negativeColor);
        }
        else if(extra.balance() > 0)
        {
            balance.setText("+" + extra.balance());
            balance.setTextColor(positiveColor);
        }
        else
        {
            balance.setText("nothing");
            balance.setTextColor(neutralColor);
        }
    }
}