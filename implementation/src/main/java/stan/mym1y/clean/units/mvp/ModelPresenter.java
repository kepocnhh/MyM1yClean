package stan.mym1y.clean.units.mvp;

public abstract class ModelPresenter<VIEW, MODEL>
        extends Presenter<VIEW>
{
    private final MODEL model;
    public ModelPresenter(VIEW v, MODEL m)
    {
        super(v);
        model = m;
    }
    final protected MODEL model()
    {
        return model;
    }
}