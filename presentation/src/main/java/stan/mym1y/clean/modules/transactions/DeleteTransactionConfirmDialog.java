package stan.mym1y.clean.modules.transactions;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import stan.mym1y.clean.App;
import stan.mym1y.clean.R;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.units.dialogs.UtilDialog;

public class DeleteTransactionConfirmDialog
        extends UtilDialog
{
    static public UtilDialog newInstance(Listener l)
    {
        DeleteTransactionConfirmDialog fragment = new DeleteTransactionConfirmDialog();
        fragment.listener = l;
        return fragment;
    }

    private View background;
    private TextView title_text;
    private TextView message_text;
    private TextView cancel;
    private TextView confirm;

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
        background = findView(R.id.background);
        title_text = findView(R.id.title_text);
        message_text = findView(R.id.message_text);
        cancel = findView(R.id.cancel);
        confirm = findView(R.id.confirm);
        setClickListener(cancel, confirm);
    }
    protected void init()
    {
        setTheme(App.component().themeSwitcher().theme());
    }
    private void setTheme(Theme theme)
    {
        background.setBackgroundColor(theme.colors().background());
        title_text.setTextColor(theme.colors().foreground());
        message_text.setTextColor(theme.colors().foreground());
        cancel.setTextColor(theme.colors().foreground());
        confirm.setTextColor(theme.colors().alert());
    }

    public interface Listener
    {
        void confirm();
    }
}