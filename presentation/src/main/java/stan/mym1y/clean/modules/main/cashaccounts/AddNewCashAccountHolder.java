package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.units.adapters.Holder;

class AddNewCashAccountHolder
        extends Holder
{
    private final Theme theme;

    private final ImageView add_new_cash_account;

    AddNewCashAccountHolder(Context context, ViewGroup parent, Theme t)
    {
        super(context, parent, R.layout.cashaccount_add_new_list_item);
        theme = t;
        add_new_cash_account = view(R.id.add_new_cash_account);
        setTheme();
    }
    private void setTheme()
    {
        add_new_cash_account.setColorFilter(theme.colors().foreground());
    }

    void setClick(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }
}