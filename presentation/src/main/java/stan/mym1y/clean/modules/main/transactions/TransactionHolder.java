package stan.mym1y.clean.modules.main.transactions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import stan.mym1y.clean.R;
import stan.mym1y.clean.units.adapters.Holder;

class TransactionHolder
    extends Holder
{
    private final TextView count;
    private final TextView date;

    private final int positiveColor;
    private final int negativeColor;

    TransactionHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.transaction_list_item);
        count = view(R.id.count);
        date = view(R.id.date);
        positiveColor = context.getResources().getColor(R.color.green);
        negativeColor = context.getResources().getColor(R.color.red);
    }

    void setLongClick(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
    }
    void setCount(int c)
    {
        if(c < 0)
        {
            count.setText(String.valueOf(c));
            count.setTextColor(negativeColor);
        }
        else
        {
            count.setText("+" + c);
            count.setTextColor(positiveColor);
        }
    }
    void setDate(long d)
    {
        Date dt = new Date(d);
        date.setText(proxy((dt.getYear()-100)) + "." + proxy((dt.getMonth()+1)) + "." + proxy(dt.getDate()) + " " + proxy(dt.getHours()) + ":" + proxy(dt.getMinutes()) + ":" + proxy(dt.getSeconds()));
    }
    private String proxy(int num)
    {
        return num < 10 ? "0"+num : ""+num;
    }
}