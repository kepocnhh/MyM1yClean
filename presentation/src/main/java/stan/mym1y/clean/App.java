package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.cases.Cases;
import stan.mym1y.clean.components.AppComponent;
import stan.mym1y.clean.components.FoldersAccess;
import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.components.MainComponent;
import stan.mym1y.clean.cores.ui.Theme;
import stan.mym1y.clean.jsonorg.JSON;
import stan.mym1y.clean.managers.FoldersManager;
import stan.mym1y.clean.managers.SecurityManager;
import stan.mym1y.clean.modules.ui.ColorsData;
import stan.mym1y.clean.modules.ui.ThemeData;
import stan.mym1y.clean.okhttp.OkHttp;
import stan.mym1y.clean.ui.ThemeToggle;

public class App
        extends Application
{
    static private AppComponent appComponent;
    static public AppComponent component()
    {
        return appComponent;
    }

    public void onCreate()
    {
        super.onCreate();
        JsonConverter jsonConverter = new JSON();
        FoldersAccess foldersAccess = new FoldersManager(getApplicationContext().getFilesDir().getAbsolutePath());
        Theme lightTheme = new ThemeData(false, new ColorsData(color(R.color.white),
                color(R.color.graydark),
                color(R.color.blue),
                color(R.color.green),
                color(R.color.red),
                color(R.color.gray),
                color(R.color.red),
                color(R.color.blue)));
//        Theme lightTheme = new ThemeData(new ColorsData(color(R.color.yellow),
//                color(R.color.blue),
//                color(R.color.orange),
//                color(R.color.indigo),
//                color(R.color.purple),
//                color(R.color.graydark),
//                color(R.color.red),
//                color(R.color.green)));
        Theme darkTheme = new ThemeData(true, new ColorsData(color(R.color.graydark),
                color(R.color.white),
                color(R.color.blue),
                color(R.color.green),
                color(R.color.red),
                color(R.color.graylight),
                color(R.color.red),
                color(R.color.blue)));
        appComponent = new MainComponent(new Boxes(foldersAccess.getDataBasePath()),
                new OkHttp(jsonConverter),
                jsonConverter, foldersAccess,
                new Cases(foldersAccess.getDataBasePath(), darkTheme, lightTheme),
                new SecurityManager(),
                new ThemeToggle(lightTheme));
    }

    private int color(int colorId)
    {
        return getApplicationContext().getResources().getColor(colorId);
    }
}