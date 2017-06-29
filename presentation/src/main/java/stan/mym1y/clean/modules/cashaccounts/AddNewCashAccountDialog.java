package stan.mym1y.clean.modules.cashaccounts;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import stan.mym1y.clean.R;
import stan.mym1y.clean.units.dialogs.UtilDialog;

public class AddNewCashAccountDialog
        extends UtilDialog
{
    static public UtilDialog newInstance(Listener l)
    {
        AddNewCashAccountDialog fragment = new AddNewCashAccountDialog();
        fragment.listener = l;
        return fragment;
    }

    private EditText title;

    private Listener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), R.style.Dialog);
    }

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.add:
                newCashAccount();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.add_new_cash_account_dialog;
    }
    protected void initViews(View v)
    {
        title = findView(R.id.title);
        setClickListener(findView(R.id.add), findView(R.id.cancel));
    }
    protected void init()
    {
    }

    private void newCashAccount()
    {
        String t = title.getText().toString();
        if(!t.isEmpty())
        {
            listener.newCashAccount(t);
            dismiss();
        }
    }

    public interface Listener
    {
        void newCashAccount(String title);
    }
}