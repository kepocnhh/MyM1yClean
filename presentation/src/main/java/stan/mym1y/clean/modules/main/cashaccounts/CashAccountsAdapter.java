package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;

public class CashAccountsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private final Context context;
    private final Listener listener;
    private List<CashAccount> data;

    public CashAccountsAdapter(Context c, Listener l)
    {
        context = c;
        listener = l;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case ViewTypes.ADD_NEW:
                return new AddNewCashAccountHolder(context, parent);
            case ViewTypes.NORMAL:
                return new CashAccountHolder(context, parent);
        }
        throw new RuntimeException("view type " + viewType + " not recognized!");
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position == getItemCount()-1)
        {
            onBindViewHolder((AddNewCashAccountHolder)holder);
        }
        else
        {
            onBindViewHolder((CashAccountHolder)holder, position);
        }
    }
    private void onBindViewHolder(AddNewCashAccountHolder holder)
    {
        holder.setClick(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                listener.addNewCashAccount();
            }
        });
    }
    private void onBindViewHolder(CashAccountHolder holder, int position)
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
        return data.size() + 1;
    }
    public int getItemViewType(int position)
    {
        if(position == getItemCount()-1)
        {
            return ViewTypes.ADD_NEW;
        }
        else
        {
            return ViewTypes.NORMAL;
        }
    }
    public void swapData(List<CashAccount> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
    }

    private interface ViewTypes
    {
        int ADD_NEW = 1;
        int NORMAL = 2;
    }

    public interface Listener
    {
        void delete(CashAccount cashAccount);
        void cashAccount(CashAccount cashAccount);
        void addNewCashAccount();
    }
}