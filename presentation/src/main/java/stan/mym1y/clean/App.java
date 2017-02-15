package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.di.AppComponent;
import stan.mym1y.clean.di.FoldersAccess;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.managers.FoldersManager;
import stan.mym1y.clean.managers.PreferenceManager;

public class App
        extends Application
{
    static private AppComponent appComponent;
    static public AppComponent getAppComponent()
    {
        return appComponent;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        FoldersAccess foldersAccess = new FoldersManager(getApplicationContext().getFilesDir().getAbsolutePath());
        appComponent = new Component(new Boxes(foldersAccess.getDataBasePath()), foldersAccess, new PreferenceManager(getApplicationContext()));
    }

    private final class Component
        implements AppComponent
    {
        private DAO dataAccess;
        private FoldersAccess foldersAccess;
        private Settings settings;

        Component(DAO dao, FoldersAccess fAccess, Settings ss)
        {
            dataAccess = dao;
            foldersAccess = fAccess;
            settings = ss;
        }

        @Override
        public DAO getDataAccess()
        {
            return dataAccess;
        }
        @Override
        public FoldersAccess getFoldersAccess()
        {
            return foldersAccess;
        }
        @Override
        public Settings getSettings()
        {
            return settings;
        }
    }
}