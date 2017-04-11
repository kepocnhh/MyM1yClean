package stan.mym1y.clean.modules.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stan.mym1y.clean.cores.transactions.Transaction;

class TransactionsAdapter
        extends RecyclerView.Adapter<TransactionsHolder>
{
    private final Context context;
    private final TransactionsAdapterListener listener;
    private List<Transaction> data;

    TransactionsAdapter(Context c, TransactionsAdapterListener l)
    {
        context = c;
        listener = l;
    }

    public TransactionsHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new TransactionsHolder(context, parent);
    }

    public void onBindViewHolder(TransactionsHolder holder, int position)
    {
        final Transaction transaction = data.get(position);
        holder.setCount(transaction.getCount());
        holder.setDate(transaction.getDate());
        holder.setLongClick(new View.OnLongClickListener()
        {
            public boolean onLongClick(View view)
            {
                listener.delete(transaction.getId());
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
        return data.size();
    }
    void swapData(List<Transaction> d)
    {
        if(this.data != null)
        {
            this.data.clear();
        }
        this.data = d;
    }
}