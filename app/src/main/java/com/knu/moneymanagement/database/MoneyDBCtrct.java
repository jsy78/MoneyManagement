package com.knu.moneymanagement.database;

public class MoneyDBCtrct {

    public static final String TBL_MONEY = "MONEY_T" ;
    public static final String COL_ID = "ID";
    public static final String COL_CATEGORY = "CATEGORY" ;
    public static final String COL_YEAR = "YEAR" ;
    public static final String COL_MONTH = "MONTH" ;
    public static final String COL_DAY = "DAY" ;
    public static final String COL_DETAIL = "DETAIL" ;
    public static final String COL_MONEY = "MONEY" ;

    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS " + TBL_MONEY + " " +
            "(" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + ", " +
            COL_CATEGORY + " TEXT NOT NULL" + ", " +
            COL_YEAR + " INTEGER NOT NULL" + ", " +
            COL_MONTH + " INTEGER NOT NULL" + ", " +
            COL_DAY + " INTEGER NOT NULL" + ", " +
            COL_DETAIL + " TEXT NOT NULL" + ", " +
            COL_MONEY + " INTEGER NOT NULL" +
            ")" ;
    // DROP TABLE IF EXISTS MONEY_T
    public static final String SQL_DROP_TBL = "DROP TABLE IF EXISTS " + TBL_MONEY;
    // SELECT * FROM MONEY_T
    public static final String SQL_SELECT = "SELECT * FROM " + TBL_MONEY;
    // INSERT INTO MONEY_T (CATEGORY, YEAR, MONTH, DAY, DETAIL, MONEY) VALUES (x, x, x, x, x, x)
    public static final String SQL_INSERT = "INSERT INTO " + TBL_MONEY + " " +
            "(" + COL_CATEGORY + ", " + COL_YEAR + ", " + COL_MONTH + ", " + COL_DAY + ", " + COL_DETAIL + ", " + COL_MONEY + ") VALUES " ;
    // DELETE FROM MONEY_T
    public static final String SQL_DELETE = "DELETE FROM " + TBL_MONEY;
    // UPDATE MONEY_T SET
    public static final String SQL_UPDATE = "UPDATE " + TBL_MONEY + " SET ";
}
