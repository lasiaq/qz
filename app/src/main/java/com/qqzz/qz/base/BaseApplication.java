package com.qqzz.qz.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {


    public static BaseApplication instance;
    //数据库
    //private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static Context getAppContext() {
        return instance;
    }

    /**
     * 配置数据库
     */
   /* private void setupDatabase() {
        //创建数据库shop.db
        BaseOpenHelper helper = new BaseOpenHelper(this, BaseOpenHelper.DB_NAME, null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstance() {
        return daoSession;
    }*/
}
