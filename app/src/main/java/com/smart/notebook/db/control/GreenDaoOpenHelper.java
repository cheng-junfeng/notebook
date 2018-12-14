package com.smart.notebook.db.control;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.smart.notebook.db.entity.DaoMaster;
import com.smart.notebook.db.entity.DataEntityDao;

import org.greenrobot.greendao.database.Database;


public class GreenDaoOpenHelper extends DaoMaster.OpenHelper {

    public GreenDaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, DataEntityDao.class);
    }
}