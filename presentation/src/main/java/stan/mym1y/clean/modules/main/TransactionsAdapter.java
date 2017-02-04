package stan.mym1y.clean.modules.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.ListModel;

class TransactionsAdapter
        extends RecyclerView.Adapter<TransactionsHolder>
{
    private final Context context;
    private ListModel<TransactionModel> data;

    TransactionsAdapter(Context c)
    {
        context = c;
    }

    @Override
    public TransactionsHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new TransactionsHolder(context, parent);
    }

    @Override
    public void onBindViewHolder(TransactionsHolder holder, int position)
    {
        holder.setCount(data.get(position).getCount());
        holder.setDate(data.get(position).getDate());
    }

    @Override
    public int getItemCount()
    {
        if(data == null)
        {
            return 0;
        }
        return data.size();
    }
    void swapData(ListModel<TransactionModel> d)
    {
        if(this.data != null)
        {
            this.data.clear();
        }
        this.data = d;
    }
}