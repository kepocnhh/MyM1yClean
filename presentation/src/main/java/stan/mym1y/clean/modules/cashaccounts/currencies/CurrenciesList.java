package stan.mym1y.clean.modules.cashaccounts.currencies;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import stan.mym1y.clean.cores.currencies.Currency;
import stan.mym1y.clean.cores.ui.Theme;

public class CurrenciesList
{
    private final CurrenciesAdapter currenciesAdapter;

    public CurrenciesList(Context context, RecyclerView currencies, Theme theme, Listener listener)
    {
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(context);
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        currencies.setLayoutManager(cashAccountsLinearLayoutManager);
        currenciesAdapter = new CurrenciesAdapter(context, theme, listener);
        currencies.setAdapter(currenciesAdapter);
    }

    public void swapData(List<Currency> data)
    {
        currenciesAdapter.swapData(data);
        currenciesAdapter.notifyDataSetChanged();
    }

    public interface Listener
    {
        void currency(Currency currency);
    }
}