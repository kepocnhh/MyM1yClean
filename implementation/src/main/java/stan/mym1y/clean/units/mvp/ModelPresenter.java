package stan.mym1y.clean.units.mvp;

public abstract class ModelPresenter<VIEW, MODEL>
        extends Presenter<VIEW>
{
    private MODEL model;
    public ModelPresenter(VIEW v, MODEL m)
    {
        super(v);
        model = m;
    }
    final protected MODEL getModel()
    {
        return model;
    }
}