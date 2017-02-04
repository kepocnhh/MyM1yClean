package stan.mym1y.clean.db;

import stan.mym1y.clean.dao.Models;

public interface Tables
{
    interface Transactions
            extends Models.Transactions
    {
        String TABLE_NAME = Transactions.class.getCanonicalName()
                                       .toLowerCase()
                                       .replace('.', '_') + "_table";
        String CREATE_TABLE = "create table if not exists " + TABLE_NAME + " (" +
                Columns.id + " integer primary key autoincrement, " +
                Columns.date + " integer" + "," +
                Columns.count + " integer" + //"," +
                ");";
        interface Columns
        {
            String id = TABLE_NAME + "_" + "id";
            String date = TABLE_NAME + "_" + "date";
            String count = TABLE_NAME + "_" + "count";
        }
    }
}