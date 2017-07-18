package stan.mym1y.clean.modules.main.transactions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.units.adapters.Holder;

class TransactionHolder
    extends Holder
{
    private final Theme theme;

    private final TextView count;
    private final TextView cash_account;
    private final TextView date;
    private final View divider;

    TransactionHolder(Context context, ViewGroup parent, Theme t)
    {
        super(context, parent, R.layout.transaction_list_item);
        theme = t;
        count = view(R.id.count);
        cash_account = view(R.id.cash_account);
        date = view(R.id.date);
        divider = view(R.id.divider);
        setTheme();
    }
    private void setTheme()
    {
        cash_account.setTextColor(theme.colors().foreground());
        date.setTextColor(theme.colors().foreground());
        divider.setBackgroundColor(theme.colors().foreground());
    }

    void setLongClick(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
    }
    void render(Transaction transaction, Transaction.Extra extra)
    {
        String left = transaction.income() ? "+" : "-";
        String middle = String.valueOf(Math.abs(transaction.count()));
        String right = extra.currency().codeName();
        switch(extra.currency().minorUnitType())
        {
            case TEN:
                middle += "." + String.valueOf(transaction.minorCount());
                break;
            case HUNDRED:
                middle += "." + (transaction.minorCount() < 10 ? "0" + transaction.minorCount() : transaction.minorCount());
                break;
        }
        count.setText(left + middle + " " + right);
        count.setTextColor(transaction.income() ? theme.colors().positive() : theme.colors().negative());
        Date dt = new Date(transaction.date());
        date.setText(proxy((dt.getYear()-100)) + "." + proxy((dt.getMonth()+1)) + "." + proxy(dt.getDate()) + " " + proxy(dt.getHours()) + ":" + proxy(dt.getMinutes()) + ":" + proxy(dt.getSeconds()));
        cash_account.setText(extra.cashAccountTitle());
    }
    private String proxy(int num)
    {
        return (num < 10 ? "0" : "") + num;
    }
}