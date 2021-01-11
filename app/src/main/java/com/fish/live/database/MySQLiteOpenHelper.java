package com.fish.live.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fish.live.database.db.DaoMaster;


public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        MigrationHelper.migrate(db, ExchangeOilFriendDao.class, SearchLinesHistoryBeanDao.class, SearchStationHistoryBeanDao.class);
    }
}
