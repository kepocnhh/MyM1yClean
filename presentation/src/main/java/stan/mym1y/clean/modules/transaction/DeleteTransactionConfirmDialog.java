package stan.mym1y.clean.modules.transaction;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stan.mym1y.clean.R;

public class DeleteTransactionConfirmDialog
        extends DialogFragment
{
    static public DialogFragment newInstanse(DeleteTransactionConfirmListener l)
    {
        DeleteTransactionConfirmDialog fragment = new DeleteTransactionConfirmDialog();
        fragment.listener = l;
        return fragment;
    }

    private View mainView;
    private DeleteTransactionConfirmListener listener;

    private final View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch(view.getId())
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
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), R.style.Dialog);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.delete_transaction_confirm_dialog, container, false);
        initViews(mainView);
        init();
        return mainView;
    }
    private void initViews(View v)
    {
        setClickListener(findView(R.id.confirm), findView(R.id.cancel));
    }
    private void init()
    {
    }

    protected <VIEW extends View> VIEW findView(int id)
    {
        return (VIEW)mainView.findViewById(id);
    }
    protected void setClickListener(View... views)
    {
        for(View v : views)
        {
            if(v != null)
            {
                v.setOnClickListener(clickListener);
            }
        }
    }

    public interface DeleteTransactionConfirmListener
    {
        void confirm();
    }
}