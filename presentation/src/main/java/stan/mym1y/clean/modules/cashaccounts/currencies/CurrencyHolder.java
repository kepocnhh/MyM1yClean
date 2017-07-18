package stan.mym1y.clean.modules.cashaccounts.currencies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.units.adapters.Holder;

class CurrencyHolder
        extends Holder
{
    private final Theme theme;

    private final TextView code_name;

    CurrencyHolder(Context context, ViewGroup parent, Theme t)
    {
        super(context, parent, R.layout.currency_select_list_item);
        theme = t;
        code_name = view(R.id.code_name);
    }

    void setClick(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }
    void render(Currency cashAccount, boolean select)
    {
        code_name.setText(cashAccount.codeName());
        code_name.setTextColor(select ? theme.colors().accent() : theme.colors().foreground());
    }
}