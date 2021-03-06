package stan.mym1y.clean.modules.main.balances;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.utils.FontChangeCrawler;

public class BalancesAdapter
    extends BaseAdapter
{
    private final Context context;
    private final FontChangeCrawler fontChangeCrawler;
    private List<CashAccount.Extra> data;

    private final String nothing_label;
    private final int positiveColor;
    private final int neutralColor;
    private final int negativeColor;

    public BalancesAdapter(Context c)
    {
        context = c;
        fontChangeCrawler = new FontChangeCrawler(context.getAssets(), "fonts/main.otf");
        nothing_label = context.getResources().getString(R.string.nothing_label);
        positiveColor = context.getResources().getColor(R.color.green);
        neutralColor = context.getResources().getColor(R.color.black);
        negativeColor = context.getResources().getColor(R.color.red);
    }

    public int getCount()
    {
        if(data == null)
        {
            return 0;
        }
        return data.size();
    }
    public CashAccount.Extra getItem(int position)
    {
        return data.get(position);
    }
    public long getItemId(int position)
    {
        return position;
    }
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        CashAccount.Extra balance = data.get(position);
        View itemView = LayoutInflater.from(context).inflate(R.layout.balance_item, parent, false);
        fontChangeCrawler.replaceFonts(itemView);
        TextView balance_value = ((TextView)itemView.findViewById(R.id.balance_value));
        if(balance.count() == 0 && balance.minorCount() == 0)
        {
            balance_value.setText(nothing_label + " " + balance.currency().codeName());
            balance_value.setTextColor(neutralColor);
            return itemView;
        }
        String left = balance.income() ? "+" : "-";
        String middle = String.valueOf(Math.abs(balance.count()));
        String right = balance.currency().codeName();
        switch(balance.currency().minorUnitType())
        {
            case TEN:
                middle += "." + String.valueOf(balance.minorCount());
                break;
            case HUNDRED:
                middle += "." + (balance.minorCount() < 10 ? "0" + balance.minorCount() : balance.minorCount());
                break;
        }
        balance_value.setText(left + middle + " " + right);
        balance_value.setTextColor(balance.income() ? positiveColor : negativeColor);
        return itemView;
    }
    public View getView(int position, View view, ViewGroup parent)
    {
        CashAccount.Extra balance = data.get(position);
        View itemView = LayoutInflater.from(context).inflate(R.layout.balance_dropdown_item, parent, false);
        fontChangeCrawler.replaceFonts(itemView);
        TextView balance_value = ((TextView)itemView.findViewById(R.id.balance_value));
        if(balance.count() == 0 && balance.minorCount() == 0)
        {
            balance_value.setText(nothing_label + " " + balance.currency().codeName());
            balance_value.setTextColor(neutralColor);
            return itemView;
        }
        String left = balance.income() ? "+" : "-";
        String middle = String.valueOf(Math.abs(balance.count()));
        String right = balance.currency().codeName();
        switch(balance.currency().minorUnitType())
        {
            case TEN:
                middle += "." + String.valueOf(balance.minorCount());
                break;
            case HUNDRED:
                middle += "." + (balance.minorCount() < 10 ? "0" + balance.minorCount() : balance.minorCount());
                break;
        }
        balance_value.setText(left + middle + " " + right);
        balance_value.setTextColor(balance.income() ? positiveColor : negativeColor);
        return itemView;
    }

    public void swapData(List<CashAccount.Extra> d)
    {
        if(data != null)
        {
            data.clear();
        }
        data = d;
    }
}