package stan.mym1y.clean.modules.transaction;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import stan.mym1y.clean.R;

public class AddNewTransactionDialog
        extends DialogFragment
{
    static public DialogFragment newInstanse(AddNewTransactionListener l)
    {
        AddNewTransactionDialog fragment = new AddNewTransactionDialog();
        fragment.listener = l;
        return fragment;
    }

    private View mainView;
    private ImageView side;
    private EditText count;

    private final View.OnClickListener clickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            switch(view.getId())
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
    };

    private AddNewTransactionListener listener;
    private boolean positive;
    private Drawable positiveDrawable;
    private Drawable negativeDrawable;
    private int positiveColor;
    private int negativeColor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new Dialog(getActivity(), R.style.Dialog);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mainView = inflater.inflate(R.layout.add_new_transaction_dialog, container, false);
        initViews(mainView);
        init();
        return mainView;
    }
    private void initViews(View v)
    {
        count = findView(R.id.count);
        side = findView(R.id.side);
        setClickListener(findView(R.id.add), findView(R.id.cancel), side);
    }
    private void init()
    {
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
            listener.newTransaction(positive ? c : -c);
            dismiss();
        }
        catch(NumberFormatException e)
        {

        }
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

    public interface AddNewTransactionListener
    {
        void newTransaction(int count);
    }
}