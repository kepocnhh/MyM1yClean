package stan.mym1y.clean.modules.main.transactions;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import stan.mym1y.clean.cores.transactions.Transaction;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.data.Pair;

public class TransactionsList
{
    private final RecyclerView transactions;
    private final TransactionsAdapter transactionsAdapter;

    public TransactionsList(Context context, RecyclerView ts, Theme theme, Listener listener)
    {
        transactions = ts;
        transactions.setLayoutManager(new TransactionsLayoutManager(context));
        transactionsAdapter = new TransactionsAdapter(context, theme, listener);
        transactions.setAdapter(transactionsAdapter);
    }

    public void hide()
    {
        transactions.setVisibility(View.GONE);
    }
    public void swapData(List<Pair<Transaction, Transaction.Extra>> data)
    {
        transactions.setVisibility(View.VISIBLE);
        transactionsAdapter.swapData(data);
        transactionsAdapter.notifyDataSetChanged();
    }

    public interface Listener
    {
        void delete(Transaction transaction);
    }

    private class TransactionsLayoutManager
        extends LinearLayoutManager
    {
        TransactionsLayoutManager(Context context)
        {
            super(context);
        }

        public boolean canScrollHorizontally()
        {
            return false;
        }
    }
}