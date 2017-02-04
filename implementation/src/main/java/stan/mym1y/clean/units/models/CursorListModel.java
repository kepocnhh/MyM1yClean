package stan.mym1y.clean.units.models;

import android.database.Cursor;

import stan.mym1y.clean.dao.ListModel;

public abstract class CursorListModel<MODEL>
        implements ListModel<MODEL>
{
    private Cursor data;

    public CursorListModel(Cursor d)
    {
        this.data = d;
    }

    @Override
    public MODEL get(int i)
    {
        if(size() > i && data.getPosition() == i)
        {
            return getModel();
        }
        else if(data.moveToPosition(i))
        {
            return getModel();
        }
        return null;
    }
    protected abstract MODEL getModel();
    @Override
    public int size()
    {
        if(data == null)
        {
            return 0;
        }
        return data.getCount();
    }
    @Override
    public void clear()
    {
        if(data != null)
        {
            data.close();
            data = null;
        }
    }
    protected int getColumnIndex(String ci)
    {
        return data.getColumnIndex(ci);
    }
    protected int getInt(String ci)
    {
        return data.getInt(getColumnIndex(ci));
    }
    protected String getString(String ci)
    {
        return data.getString(getColumnIndex(ci));
    }
    protected long getLong(String ci)
    {
        return data.getLong(getColumnIndex(ci));
    }
}