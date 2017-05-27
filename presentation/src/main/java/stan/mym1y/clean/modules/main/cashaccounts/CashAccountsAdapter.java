package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public class CashAccountsAdapter
        extends RecyclerView.Adapter<CashAccountHolder>
{
    private final Context context;
    private final Listener listener;
    private List<CashAccount> data;

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
        holder.render(cashAccount);
        holder.setClick(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                listener.cashAccount(cashAccount);
            }
        });
        holder.setLongClick(new View.OnLongClickListener()
        {
            public boolean onLongClick(View v)
            {
                listener.delete(cashAccount);
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
    public void swapData(List<CashAccount> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
    }

    public interface Listener
    {
        void delete(CashAccount cashAccount);
        void cashAccount(CashAccount cashAccount);
        void addNewCashAccount();
    }
}