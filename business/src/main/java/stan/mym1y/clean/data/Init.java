package stan.mym1y.clean.data;

public interface Init<D>
{
    boolean init();
    D data();
}