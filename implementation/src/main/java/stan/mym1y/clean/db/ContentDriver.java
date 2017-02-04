package stan.mym1y.clean.db;

import android.content.ContentValues;

import stan.mym1y.clean.cores.transactions.TransactionModel;

class ContentDriver
{
    ContentValues getContentValues(TransactionModel model)
    {
        ContentValues content = new ContentValues();
        if(model.getId() > 0)
        {
            content.put(Tables.Transactions.Columns.id, model.getId());
        }
        content.put(Tables.Transactions.Columns.count, model.getCount());
        content.put(Tables.Transactions.Columns.date, model.getDate());
        return content;
    }
}