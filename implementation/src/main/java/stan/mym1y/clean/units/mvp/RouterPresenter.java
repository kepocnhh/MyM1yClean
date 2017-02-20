package stan.mym1y.clean.units.mvp;

public abstract class RouterPresenter<VIEW, ROUTER>
        extends Presenter<VIEW>
{
    private final ROUTER router;
    public RouterPresenter(VIEW v, ROUTER r)
    {
        super(v);
        router = r;
    }
    final protected ROUTER getRouter()
    {
        return router;
    }
}