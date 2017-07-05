package stan.mym1y.clean.modules.cashaccounts.currencies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.units.adapters.Holder;

class CurrencyHolder
        extends Holder
{
    private final TextView code_name;

    private final int selectColor;
    private final int normalColor;

    CurrencyHolder(Context context, ViewGroup parent)
    {
        super(context, parent, R.layout.currency_select_list_item);
        code_name = view(R.id.code_name);
        selectColor = context.getResources().getColor(R.color.colorPrimary);
        normalColor = context.getResources().getColor(R.color.black);
    }

    void setClick(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }
    void render(Currency cashAccount, boolean select)
    {
        code_name.setText(cashAccount.codeName());
        code_name.setTextColor(select ? selectColor : normalColor);
    }
}