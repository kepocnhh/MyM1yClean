package stan.mym1y.clean.units.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stan.mym1y.clean.utils.FontChangeCrawler;

public class Holder
        extends RecyclerView.ViewHolder
{
    public Holder(Context context, ViewGroup parent, int layout)
    {
        super(LayoutInflater.from(context).inflate(layout, parent, false));
        new FontChangeCrawler(context.getAssets(), "fonts/main.otf").replaceFonts(itemView);
    }

    protected <VIEW extends View> VIEW view(int id)
    {
        return (VIEW)itemView.findViewById(id);
    }
}