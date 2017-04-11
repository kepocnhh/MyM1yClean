package stan.mym1y.clean.units.mvp;

public abstract class ModelRouterPresenter<VIEW, MODEL, ROUTER>
        extends ModelPresenter<VIEW, MODEL>
{
    private final ROUTER router;
    public ModelRouterPresenter(VIEW v, MODEL m, ROUTER r)
    {
        super(v, m);
        router = r;
    }
    final protected ROUTER router()
    {
        return router;
    }
}