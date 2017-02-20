package stan.mym1y.clean.utils;

import android.os.Handler;
import android.os.Looper;

import stan.mym1y.clean.di.PlatformUtil;

public class AndroidUtil
    implements PlatformUtil
{
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void runOnUiThread(Runnable r)
    {
        uiHandler.post(r);
    }
}