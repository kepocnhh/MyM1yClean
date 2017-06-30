package stan.mym1y.clean.modules.main.transactions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.units.adapters.Holder;

class TransactionHolder
    extends Holder
{
    private final TextView count;
    private final TextView cash_account;
    private final TextView date;

    private final int positiveColor;
    private final int negativeColor;

    TransactionHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.transaction_list_item);
        count = view(R.id.count);
        cash_account = view(R.id.cash_account);
        date = view(R.id.date);
        positiveColor = context.getResources().getColor(R.color.green);
        negativeColor = context.getResources().getColor(R.color.red);
    }

    void setLongClick(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
    }
    void render(Transaction transaction, Transaction.Extra extra)
    {
        if(transaction.count() < 0)
        {
            count.setText(String.valueOf(transaction.count()));
            count.setTextColor(negativeColor);
        }
        else
        {
            count.setText("+" + transaction.count());
            count.setTextColor(positiveColor);
        }
        Date dt = new Date(transaction.date());
        date.setText(proxy((dt.getYear()-100)) + "." + proxy((dt.getMonth()+1)) + "." + proxy(dt.getDate()) + " " + proxy(dt.getHours()) + ":" + proxy(dt.getMinutes()) + ":" + proxy(dt.getSeconds()));
        cash_account.setText(extra.cashAccountTitle());
    }
    private String proxy(int num)
    {
        return num < 10 ? "0"+num : ""+num;
    }
}