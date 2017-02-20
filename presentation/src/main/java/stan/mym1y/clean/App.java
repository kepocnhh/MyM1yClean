package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.di.AppComponent;
import stan.mym1y.clean.di.FoldersAccess;
import stan.mym1y.clean.di.PlatformUtil;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.managers.FoldersManager;
import stan.mym1y.clean.managers.PreferenceManager;
import stan.mym1y.clean.utils.AndroidUtil;

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
        appComponent = new Component(new Boxes(foldersAccess.getDataBasePath())
                ,foldersAccess
                ,new PreferenceManager(getApplicationContext())
                ,new AndroidUtil()
        );
    }

    private final class Component
        implements AppComponent
    {
        private DAO dataAccess;
        private FoldersAccess foldersAccess;
        private Settings settings;
        private PlatformUtil platformUtil;

        Component(DAO dao, FoldersAccess fAccess, Settings ss, PlatformUtil pu)
        {
            dataAccess = dao;
            foldersAccess = fAccess;
            settings = ss;
            platformUtil = pu;
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
        @Override
        public PlatformUtil getPlatformUtil()
        {
            return platformUtil;
        }
    }
}