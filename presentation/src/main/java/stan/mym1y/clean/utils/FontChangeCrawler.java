package stan.mym1y.clean.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontChangeCrawler
{
    private Typeface typeface;

    public FontChangeCrawler(Typeface tf)
    {
        typeface = tf;
    }
    public FontChangeCrawler(AssetManager assets, String assetsFontFileName)
    {
        this(Typeface.createFromAsset(assets, assetsFontFileName));
    }

    public void replaceFonts(View view)
    {
        if(view instanceof ViewGroup)
        {
            replaceFonts((ViewGroup)view);
        }
        else if(view instanceof TextView)
        {
            ((TextView)view).setTypeface(typeface);
        }
    }
    private void replaceFonts(ViewGroup viewGroup)
    {
        for(int i = 0; i < viewGroup.getChildCount(); ++i)
        {
            replaceFonts(viewGroup.getChildAt(i));
        }
    }
}