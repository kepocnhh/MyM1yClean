package stan.mym1y.clean.units.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class LostFocusEditText
        extends EditText
{
    private final InputMethodManager inputMethodManager;

    public LostFocusEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setOnEditorActionListener(new OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
                }
                return false;
            }
        });
    }
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}