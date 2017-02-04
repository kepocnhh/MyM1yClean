package stan.mym1y.clean.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import stan.mym1y.clean.cores.transactions.TransactionModel;
import stan.mym1y.clean.dao.DAO;
import stan.mym1y.clean.dao.ListModel;
import stan.mym1y.clean.dao.Models;
import stan.mym1y.clean.modules.transactions.Transaction;
import stan.mym1y.clean.modules.transactions.TransactionsCursorListModel;

public class SQLite
        implements DAO
{
    static private final String DB_NAME = "mym1yclean";
    static private final int DB_VERSION = 1702042301;

    private volatile SQLiteDatabase sdb;
    private final ContentDriver contentDriver = new ContentDriver();

    private final Models.Transactions transactions = new Tables.Transactions()
    {
        @Override
        public ListModel<TransactionModel> getAll()
        {
            return new TransactionsCursorListModel(sdb.rawQuery("SELECT * " + "FROM " + TABLE_NAME, null));
        }
        @Override
        public TransactionModel get(int id)
        {
            Cursor cursor = sdb.rawQuery("SELECT * " + "FROM " + TABLE_NAME,null);
            try
            {
                if(cursor.moveToFirst())
                {
                    return new Transaction(
                            cursor.getInt(cursor.getColumnIndex(Columns.id))
                            ,cursor.getInt(cursor.getColumnIndex(Columns.count))
                            ,cursor.getLong(cursor.getColumnIndex(Columns.date))
                    );
                }
                return null;
            }
            finally
            {
                cursor.close();
            }
        }
        @Override
        public void add(TransactionModel transaction)
        {
            sdb.insertWithOnConflict(TABLE_NAME, null, contentDriver.getContentValues(transaction), SQLiteDatabase.CONFLICT_REPLACE);
        }
        @Override
        public void clear()
        {
            sdb.execSQL("drop table if exists " + TABLE_NAME);
            sdb.execSQL(CREATE_TABLE);
        }
    };

    public SQLite(Context context)
    {
        sdb = new SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)
        {
            @Override
            public void onCreate(SQLiteDatabase db)
            {
                createTables(db);
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
            {
                clearTables(db);
                onCreate(db);
            }
        }.getWritableDatabase();
    }
    private void clearTables(SQLiteDatabase db)
    {
        db.execSQL("drop table if exists " + Tables.Transactions.TABLE_NAME);
    }
    private void createTables(SQLiteDatabase db)
    {
        db.execSQL(Tables.Transactions.CREATE_TABLE);
    }

    @Override
    public Models.Transactions getTransactions()
    {
        return transactions;
    }
}