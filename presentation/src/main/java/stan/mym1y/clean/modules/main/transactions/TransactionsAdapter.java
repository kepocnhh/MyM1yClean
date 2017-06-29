package stan.mym1y.clean.modules.main.transactions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stan.mym1y.clean.cores.transactions.Transaction;

public class TransactionsAdapter
        extends RecyclerView.Adapter<TransactionHolder>
{
    private final Context context;
    private final Listener listener;
    private List<Transaction> data;

    public TransactionsAdapter(Context c, Listener l)
    {
        context = c;
        listener = l;
    }

    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new TransactionHolder(context, parent);
    }

    public void onBindViewHolder(TransactionHolder holder, int position)
    {
        final Transaction transaction = data.get(position);
        holder.setCount(transaction.count());
        holder.setDate(transaction.date());
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
        return data.size();
    }
    public void swapData(List<Transaction> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
    }

    public interface Listener
    {
        void delete(Transaction transaction);
    }
}