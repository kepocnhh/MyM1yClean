package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.boxes.Cases;
import stan.mym1y.clean.connection.OkHttp;
import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.di.AppComponent;
import stan.mym1y.clean.di.Connection;
import stan.mym1y.clean.di.FoldersAccess;
import stan.mym1y.clean.di.JsonConverter;
import stan.mym1y.clean.di.PlatformUtil;
import stan.mym1y.clean.di.Settings;
import stan.mym1y.clean.managers.FoldersManager;
import stan.mym1y.clean.utils.AndroidUtil;
import stan.mym1y.clean.utils.Converter;

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
                ,new Cases(foldersAccess.getDataBasePath())
                ,new AndroidUtil()
                ,new OkHttp()
                ,new Converter()
        );
    }

    private final class Component
        implements AppComponent
    {
        private DAO dataAccess;
        private FoldersAccess foldersAccess;
        private Settings settings;
        private PlatformUtil platformUtil;
        private Connection connection;
        private JsonConverter jsonConverter;

        Component(DAO dao, FoldersAccess fAccess, Settings ss, PlatformUtil pu, Connection cn, JsonConverter jc)
        {
            dataAccess = dao;
            foldersAccess = fAccess;
            settings = ss;
            platformUtil = pu;
            connection = cn;
            jsonConverter = jc;
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
        @Override
        public Connection getConnection()
        {
            return connection;
        }
        @Override
        public JsonConverter getJsonConverter()
        {
            return jsonConverter;
        }
    }
}