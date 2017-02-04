package stan.mym1y.clean.modules.transactions;

import android.database.Cursor;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.db.Tables;
import stan.mym1y.clean.units.models.CursorListModel;

public class TransactionsCursorListModel
        extends CursorListModel<TransactionModel>
{
    private final TransactionModel model = new TransactionModel()
    {
        @Override
        public int getId()
        {
            return getInt(Tables.Transactions.Columns.id);
        }
        @Override
        public long getDate()
        {
            return getLong(Tables.Transactions.Columns.date);
        }
        @Override
        public int getCount()
        {
            return getInt(Tables.Transactions.Columns.count);
        }
    };

    public TransactionsCursorListModel(Cursor d)
    {
        super(d);
    }

    @Override
    protected TransactionModel getModel()
    {
        return model;
    }
}