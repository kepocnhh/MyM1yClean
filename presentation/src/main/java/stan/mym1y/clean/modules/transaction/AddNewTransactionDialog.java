package stan.mym1y.clean.modules.transaction;

import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import stan.mym1y.clean.R;
import stan.mym1y.clean.units.dialogs.UtilDialog;

public class AddNewTransactionDialog
        extends UtilDialog
{
    static public UtilDialog newInstanse(Listener l)
    {
        AddNewTransactionDialog fragment = new AddNewTransactionDialog();
        fragment.listener = l;
        return fragment;
    }

    private ImageView side;
    private EditText count;

    private Listener listener;
    private boolean positive;
    private Drawable positiveDrawable;
    private Drawable negativeDrawable;
    private int positiveColor;
    private int negativeColor;

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
        count = findView(R.id.count);
        side = findView(R.id.side);
        setClickListener(findView(R.id.add), findView(R.id.cancel), side);
    }
    protected void init()
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

    public interface Listener
    {
        void newTransaction(int count);
    }
}