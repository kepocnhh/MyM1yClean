package stan.mym1y.clean.modules.transaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import stan.mym1y.clean.R;
import stan.mym1y.clean.units.dialogs.UtilDialog;

public class DeleteTransactionConfirmDialog
        extends UtilDialog
{
    static public UtilDialog newInstanse(Listener l)
    {
        DeleteTransactionConfirmDialog fragment = new DeleteTransactionConfirmDialog();
        fragment.listener = l;
        return fragment;
    }

    private Listener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), R.style.Dialog);
    }

    protected void onClickView(int id)
    {
        switch(id)
        {
            case R.id.confirm:
                listener.confirm();
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }
    protected int getContentView()
    {
        return R.layout.delete_transaction_confirm_dialog;
    }
    protected void initViews(View v)
    {
        setClickListener(findView(R.id.confirm), findView(R.id.cancel));
    }
    protected void init()
    {
    }

    public interface Listener
    {
        void confirm();
    }
}