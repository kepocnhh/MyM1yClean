package stan.mym1y.clean.managers;

import android.util.Log;

import java.io.File;

import stan.mym1y.clean.components.FoldersAccess;

public class FoldersManager
    implements FoldersAccess
{
    private String filesDirectory;

    public FoldersManager(String fd)
    {
        filesDirectory = fd;
        checkRoot();
        Log.e(getClass().getName(),"filesDir - " + filesDirectory);
    }
    private void checkRoot()
    {
        File dataBasePath = new File(getDataBasePath());
        dataBasePath.mkdirs();
        if(dataBasePath.exists())
        {
            Log.e(getClass().getName(), "dataBasePath - " + getDataBasePath());
        }
        else
        {
            Log.e(getClass().getName(), "create dataBasePath failed!!!");
        }
    }

    public String getDataBasePath()
    {
        return filesDirectory + "/database";
    }
}