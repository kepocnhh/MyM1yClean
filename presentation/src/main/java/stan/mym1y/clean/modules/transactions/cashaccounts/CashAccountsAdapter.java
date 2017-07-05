package stan.mym1y.clean.modules.transactions.cashaccounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.units.adapters.Holder;

public class CashAccountsAdapter
        extends RecyclerView.Adapter<CashAccountsAdapter.CashAccountHolder>
{
    private final Context context;
    private final Listener listener;
    private List<CashAccount> data;
    private long selectId;

    public CashAccountsAdapter(Context c, Listener l)
    {
        context = c;
        listener = l;
    }

    public CashAccountHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new CashAccountHolder(context, parent);
    }
    public void onBindViewHolder(CashAccountHolder holder, int position)
    {
        final CashAccount cashAccount = data.get(position);
        holder.render(cashAccount, cashAccount.id() == selectId);
        holder.setClick(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                listener.cashAccount(cashAccount);
                selectId = cashAccount.id();
                notifyDataSetChanged();
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
    public void swapData(List<CashAccount> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
        if(!data.isEmpty())
        {
            selectId = data.get(0).id();
            listener.cashAccount(data.get(0));
        }
    }

    public interface Listener
    {
        void cashAccount(CashAccount cashAccount);
    }

    class CashAccountHolder
        extends Holder
    {
        private final TextView title;

        CashAccountHolder(Context context, ViewGroup parent)
        {
            super(context, parent, R.layout.cashaccount_select_list_item);
            title = view(R.id.title);
        }

        void setClick(View.OnClickListener listener)
        {
            itemView.setOnClickListener(listener);
        }
        void render(CashAccount cashAccount, boolean select)
        {
            title.setText(cashAccount.title());
            title.setTextColor(select ? context.getResources().getColor(R.color.colorPrimary) : context.getResources().getColor(R.color.black));
        }
    }
}