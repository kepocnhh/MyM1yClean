package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.cases.Cases;
import stan.mym1y.clean.components.AppComponent;
import stan.mym1y.clean.components.FoldersAccess;
import stan.mym1y.clean.components.JsonConverter;
import stan.mym1y.clean.components.MainComponent;
import stan.mym1y.clean.jsonsimple.JSON;
import stan.mym1y.clean.managers.FoldersManager;
import stan.mym1y.clean.managers.SecurityManager;
import stan.mym1y.clean.okhttp.OkHttp;

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
        appComponent = new MainComponent(new Boxes(foldersAccess.getDataBasePath()),
                new OkHttp(jsonConverter),
                jsonConverter, foldersAccess,
                new Cases(foldersAccess.getDataBasePath()),
                new SecurityManager());
    }
}