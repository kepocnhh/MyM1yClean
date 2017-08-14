package stan.mym1y.clean.modules.data;

import stan.mym1y.clean.data.Pair;

public class PairData<F, S>
    implements Pair<F, S>
{
    static public <F, S> Pair<F, S> create(F first, S second)
    {
        return new PairData<>(first, second);
    }

    private final F first;
    private final S second;

    private PairData(F f, S s)
    {
        first = f;
        second = s;
    }

    public F first()
    {
        return first;
    }
    public S second()
    {
        return second;
    }
}