package me.blue.rss;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 2017/2/12.
 */

public class RssDataBase {
    private RssDatabaseHelper RssDb;
    public RssDataBase(){
        //db=new RssDatabaseHelper(null, "test.db", null, 1);
    }

    public List<RssSource> getRssSourceList(){
        RssDb=new RssDatabaseHelper(null, "test.db", null, 1);
        SQLiteDatabase db=RssDb.getReadableDatabase();
        List<RssSource> list=new ArrayList<>();

        Cursor cursor = db.query("Source", null, null, null, null, null, null);
        if(cursor.getCount()==0)
            RssDb.InitialSource();
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.
                        getColumnIndex("name"));
                String address = cursor.getString(cursor.
                        getColumnIndex("address"));
                boolean isCheck = cursor.getInt(cursor.getColumnIndex("able"))==0? false:true ;
                list.add(new RssSource(name,address,isCheck));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
