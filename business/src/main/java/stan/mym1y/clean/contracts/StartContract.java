package stan.mym1y.clean.contracts;

import stan.mym1y.clean.cores.versions.Versions;
import stan.reactive.notify.NotifyObservable;
import stan.reactive.single.SingleObservable;

public interface StartContract
{
    interface Model
    {
        SingleObservable<Versions> getActualVersions();
        Versions getCacheVersions();
        void update(Versions versions);
        NotifyObservable updateCurrencies();
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