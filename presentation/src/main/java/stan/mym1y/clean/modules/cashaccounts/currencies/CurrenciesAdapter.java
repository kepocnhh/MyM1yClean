package stan.mym1y.clean.modules.cashaccounts.currencies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.ui.Theme;

class CurrenciesAdapter
        extends RecyclerView.Adapter<CurrencyHolder>
{
    private final Context context;
    private final Theme theme;
    private final CurrenciesList.Listener listener;
    private List<Currency> data;
    private String selectCodeNumber;

    CurrenciesAdapter(Context c, Theme t, CurrenciesList.Listener l)
    {
        context = c;
        theme = t;
        listener = l;
    }

    public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new CurrencyHolder(context, parent, theme);
    }
    public void onBindViewHolder(CurrencyHolder holder, int position)
    {
        final Currency currency = data.get(position);
        holder.render(currency, currency.codeNumber().equals(selectCodeNumber));
        holder.setClick(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                listener.currency(currency);
                selectCodeNumber = currency.codeNumber();
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
    void swapData(List<Currency> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
        if(!data.isEmpty())
        {
            selectCodeNumber = data.get(0).codeNumber();
            listener.currency(data.get(0));
        }
    }
}