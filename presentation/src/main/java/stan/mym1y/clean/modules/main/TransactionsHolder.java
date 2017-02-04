package stan.mym1y.clean.modules.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import stan.mym1y.clean.R;

class TransactionsHolder
    extends RecyclerView.ViewHolder
{
    private TextView count;
    private TextView date;

    private int positiveColor;
    private int negativeColor;

    TransactionsHolder(Context context, ViewGroup parent)
    {
        super(LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false));
        count = (TextView)itemView.findViewById(R.id.count);
        date = (TextView)itemView.findViewById(R.id.date);
        positiveColor = context.getResources().getColor(R.color.green);
        negativeColor = context.getResources().getColor(R.color.red);
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
        date.setText(dt.getDate() + "." + (dt.getMonth()+1) + "." + (dt.getYear()-100));
    }
}