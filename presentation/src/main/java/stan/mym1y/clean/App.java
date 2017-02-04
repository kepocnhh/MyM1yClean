package stan.mym1y.clean;

import android.app.Application;

import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.db.SQLite;

public class App
        extends Application
{
    static private DAO dataAcessObject;
    static public DAO getDataAcess()
    {
        return dataAcessObject;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        dataAcessObject = new SQLite(getApplicationContext());
    }
}