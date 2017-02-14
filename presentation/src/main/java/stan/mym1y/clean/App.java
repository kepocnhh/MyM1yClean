package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.boxes.Boxes;
import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.di.AppComponent;
import stan.mym1y.clean.di.FoldersAccess;
import stan.mym1y.clean.managers.FoldersManager;

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
        appComponent = new Component(new Boxes(foldersAccess.getDataBasePath()), foldersAccess);
    }

    private final class Component
        implements AppComponent
    {
        private DAO dataAccess;
        private FoldersAccess foldersAccess;

        Component(DAO dao, FoldersAccess fAccess)
        {
            dataAccess = dao;
            foldersAccess = fAccess;
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
    }
}