package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import stan.mym1y.clean.R;
import stan.mym1y.clean.units.adapters.Holder;

class AddNewCashAccountHolder
        extends Holder
{
    AddNewCashAccountHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.cashaccount_add_new_list_item);
    }

    void setClick(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }
}