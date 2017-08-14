package stan.mym1y.clean.modules.main.cashaccounts;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.data.Pair;

public class CashAccountsList
{
    private final CashAccountsAdapter cashAccountsAdapter;

    public CashAccountsList(Context context, RecyclerView cash_accounts, Theme theme, Listener listener)
    {
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(context);
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cash_accounts.setLayoutManager(cashAccountsLinearLayoutManager);
        cashAccountsAdapter = new CashAccountsAdapter(context, theme, listener);
        cash_accounts.setAdapter(cashAccountsAdapter);
    }

    public void swapData(List<Pair<CashAccount, CashAccount.Extra>> data)
    {
        cashAccountsAdapter.swapData(data);
        cashAccountsAdapter.notifyDataSetChanged();
    }

    public interface Listener
    {
        void delete(CashAccount cashAccount);
        void cashAccount(CashAccount cashAccount);
        void addNewCashAccount();
    }
}