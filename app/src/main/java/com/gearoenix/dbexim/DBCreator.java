package com.gearoenix.dbexim;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBCreator extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbexim";
    private static final int DATABASE_VERSION = 1;

    public DBCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createTable =
                "CREATE TABLE alaki (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name VARCHAR(64) NOT NULL UNIQUE);";
        database.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}