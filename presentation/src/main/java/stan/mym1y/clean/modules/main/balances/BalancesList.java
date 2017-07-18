package stan.mym1y.clean.modules.main.balances;

import android.content.Context;
import android.view.View;
import android.widget.Spinner;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.ui.Theme;

public class BalancesList
{
    private final Spinner balances;
    private final BalancesAdapter balancesAdapter;

    public BalancesList(Context context, Spinner bs, Theme theme)
    {
        balances = bs;
        balancesAdapter = new BalancesAdapter(context, theme);
        balances.setAdapter(balancesAdapter);
    }

    public void hide()
    {
        balances.setVisibility(View.GONE);
        balancesAdapter.swapData(null);
    }

    public void swapData(List<CashAccount.Extra> data)
    {
        balances.setVisibility(View.VISIBLE);
        balancesAdapter.swapData(data);
        balancesAdapter.notifyDataSetChanged();
    }
}