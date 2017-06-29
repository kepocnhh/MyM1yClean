package stan.mym1y.clean.modules.transactions;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.cashaccounts.CashAccount;
import stan.mym1y.clean.units.dialogs.UtilDialog;

public class AddNewTransactionDialog
        extends UtilDialog
{
    static public UtilDialog newInstance(Listener l)
    {
        AddNewTransactionDialog fragment = new AddNewTransactionDialog();
        fragment.listener = l;
        return fragment;
    }

    private RecyclerView cash_accounts;
    private ImageView side;
    private EditText count;

    private Listener listener;
    private CashAccountsAdapter cashAccountsAdapter;
    private boolean positive;
    private Drawable positiveDrawable;
    private Drawable negativeDrawable;
    private int positiveColor;
    private int negativeColor;
    private long cashAccountId;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), R.style.Dialog);
    }

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.add:
                newTransaction();
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.side:
                positive = !positive;
                updateSide();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.add_new_transaction_dialog;
    }
    protected void initViews(View v)
    {
        cash_accounts = findView(R.id.cash_accounts);
        count = findView(R.id.count);
        side = findView(R.id.side);
        setClickListener(findView(R.id.add), findView(R.id.cancel), side);
    }
    protected void init()
    {
        LinearLayoutManager cashAccountsLinearLayoutManager = new LinearLayoutManager(getActivity());
        cashAccountsLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        cash_accounts.setLayoutManager(cashAccountsLinearLayoutManager);
        cashAccountsAdapter = new CashAccountsAdapter(getActivity(), new CashAccountsAdapter.Listener()
        {
            public void cashAccount(CashAccount cashAccount)
            {
                log("cash account: " + cashAccount.title());
                cashAccountId = cashAccount.id();
            }
        });
        cash_accounts.setAdapter(cashAccountsAdapter);
        cashAccountsAdapter.swapData(App.component().dataLocal().cashAccountsAccess().cashAccounts().getAll());//TODO fix hardcode
        positive = true;
        positiveDrawable = getResources().getDrawable(R.mipmap.ic_add_white_24dp);
        negativeDrawable = getResources().getDrawable(R.mipmap.ic_remove_white_24dp);
        positiveColor = getResources().getColor(R.color.green);
        negativeColor = getResources().getColor(R.color.red);
        updateSide();
    }
    private void updateSide()
    {
        if(positive)
        {
            side.setImageDrawable(positiveDrawable.getConstantState().newDrawable());
            side.setColorFilter(positiveColor, PorterDuff.Mode.SRC_ATOP);
        }
        else
        {
            side.setImageDrawable(negativeDrawable.getConstantState().newDrawable());
            side.setColorFilter(negativeColor, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void newTransaction()
    {
        try
        {
            int c = Integer.parseInt(count.getText().toString());
            if(c == 0)
            {
                return;
            }
            listener.newTransaction(cashAccountId, positive ? c : -c);
            dismiss();
        }
        catch(NumberFormatException e)
        {

        }
    }

    public interface Listener
    {
        void newTransaction(long cashAccountId, int count);
    }
}