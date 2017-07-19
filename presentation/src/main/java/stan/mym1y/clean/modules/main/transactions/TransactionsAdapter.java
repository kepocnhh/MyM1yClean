package stan.mym1y.clean.modules.main.transactions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.ui.Theme;
import stan.reactive.Tuple;

class TransactionsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private final Theme theme;
    private final TransactionsList.Listener listener;
    private List<Tuple<Transaction, Transaction.Extra>> data;

    TransactionsAdapter(Context c, Theme t, TransactionsList.Listener l)
    {
        context = c;
        theme = t;
        listener = l;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case ViewTypes.FOOTER:
                return new FooterHolder(context, parent);
            case ViewTypes.NORMAL:
                return new TransactionHolder(context, parent, theme);
        }
        throw new RuntimeException("view type " + viewType + " not recognized!");
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        switch(getItemViewType(position))
        {
            case ViewTypes.FOOTER:
                break;
            case ViewTypes.NORMAL:
                onBindViewHolder((TransactionHolder)holder, position);
                break;
        }
    }
    private void onBindViewHolder(TransactionHolder holder, int position)
    {
        final Transaction transaction = data.get(position).first();
        final Transaction.Extra extra = data.get(position).second();
        holder.render(transaction, extra);
        holder.setLongClick(new View.OnLongClickListener()
        {
            public boolean onLongClick(View view)
            {
                listener.delete(transaction);
                return false;
            }
        });
    }
    public int getItemCount()
    {
        if(data == null)
        {
            return 0;
        }
        return data.size() + 1;
    }
    public int getItemViewType(int position)
    {
        if(position == getItemCount()-1)
        {
            return ViewTypes.FOOTER;
        }
        else
        {
            return ViewTypes.NORMAL;
        }
    }
    void swapData(List<Tuple<Transaction, Transaction.Extra>> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
    }

    private interface ViewTypes
    {
        int FOOTER = 1;
        int NORMAL = 2;
    }
}