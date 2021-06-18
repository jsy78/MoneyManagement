package com.knu.moneymanagement.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoneyDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1 ;//DB version 관리
    private static final String DBFILE_MONEY = "money.db" ;

    public MoneyDBHelper(Context context) {
        super(context, DBFILE_MONEY, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoneyDBCtrct.SQL_CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(MoneyDBCtrct.SQL_DROP_TBL);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // onUpgrade(db, oldVersion, newVersion);
    }

}
