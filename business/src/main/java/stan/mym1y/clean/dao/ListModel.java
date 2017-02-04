package stan.mym1y.clean.dao;

public interface ListModel<MODEL>
{
    MODEL get(int i);
    int size();
    void clear();
}