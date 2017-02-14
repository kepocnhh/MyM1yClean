package stan.mym1y.clean.units.models;

import java.util.List;

import stan.mym1y.clean.dao.ListModel;

public class ArrayListModel<MODEL>
        implements ListModel<MODEL>
{
    private List<MODEL> data;

    public ArrayListModel(List<MODEL> d)
    {
        data = d;
    }

    @Override
    public MODEL get(int i)
    {
        return data.get(i);
    }

    @Override
    public int size()
    {
        if(data == null)
        {
            return 0;
        }
        return data.size();
    }

    @Override
    public void clear()
    {
        if(data != null)
        {
            data.clear();
            data = null;
        }
    }
}