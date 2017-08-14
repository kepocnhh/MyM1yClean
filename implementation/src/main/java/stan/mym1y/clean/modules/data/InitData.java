package stan.mym1y.clean.modules.data;

import stan.mym1y.clean.data.Init;

public class InitData<D>
    implements Init<D>
{
    static public <D> Init<D> create(boolean i, D d)
    {
        return new InitData<>(i, d);
    }
    static public <D> Init<D> create(D d)
    {
        return new InitData<>(d);
    }

    private final boolean init;
    private final D data;

    private InitData(D d)
    {
        this(false, d);
    }
    private InitData(boolean i, D d)
    {
        init = i;
        data = d;
    }

    public boolean init()
    {
        return init;
    }
    public D data()
    {
        return data;
    }
}