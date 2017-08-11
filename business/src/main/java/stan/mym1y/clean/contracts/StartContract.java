package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.versions.Versions;

public interface StartContract
{
    interface Model
    {
        Versions getActualVersions()
                throws ErrorsContract.NetworkException, ErrorsContract.UnknownException;
        Versions getCacheVersions();
        void update(Versions versions);
        void updateCurrencies()
                throws ErrorsContract.UnknownException;
    }
    interface View
    {
        void complete();
        void error(CantContinueWithoutDataException e);
        void error(DataNeedUpdateException e);
        void error();
    }
    interface Presenter
    {
        void checkSync();
    }

    interface Behaviour
    {
        void sync();
    }

    class DataNeedUpdateException
            extends Exception
    {
    }
    class CantContinueWithoutDataException
            extends Exception
    {
    }
}