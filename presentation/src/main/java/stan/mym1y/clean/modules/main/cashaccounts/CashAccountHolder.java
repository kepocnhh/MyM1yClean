package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.units.adapters.Holder;

class CashAccountHolder
        extends Holder
{
//    private final TextView id;
    private final TextView title;

    CashAccountHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.cashaccount_list_item);
//        id = view(R.id.id);
        title = view(R.id.title);
    }

    void setClick(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }
    void setLongClick(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
    }
    void render(CashAccount cashAccount)
    {
//        id.setText(""+cashAccount.id());
        title.setText(""+cashAccount.title());
    }
}