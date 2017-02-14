package stan.mym1y.clean.di;

import stan.mym1y.clean.dao.DAO;

public interface AppComponent
{
    DAO getDataAccess();
    FoldersAccess getFoldersAccess();
}