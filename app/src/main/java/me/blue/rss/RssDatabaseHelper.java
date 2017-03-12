package me.blue.rss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.util.zip.DataFormatException;

/**
 * Created by blue on 2017/2/9.
 */

public class RssDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String CREAT_RSS_SOURCE="create table Source ("
            +"name text primary key,"
            +"address text,"
            +"able integer)";

    private static final String CREAD_RSS_ITEM="create table RssItem ("
            +"id integer primary key autoincrement,"
            +"title text,"
            +"link text,"
            +"pubDate text,"
            +"description text,"
            +"content text) ";



    public RssDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_RSS_SOURCE);
        db.execSQL(CREAD_RSS_ITEM);
        Log.d("sqr","creat db");
        //InitialSource();

        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    public void ClearRssItems(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete("RssItem",null,null);
    }

    public List<RssItem> LoadRssItems() throws ParseException {
        List<RssItem> rssItemsList= new ArrayList<RssItem>();
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = db.query("RssItem", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                RssItem rssItem=new RssItem();
                // 遍历Cursor对象，取出数据并打印
                String title = cursor.getString(cursor.
                        getColumnIndex("title"));
                rssItem.setTitle(title);

                String link = cursor.getString(cursor.
                        getColumnIndex("link"));
                rssItem.setLink(link);

                String dataStr=cursor.getString(cursor.
                        getColumnIndex("pubDate"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
                Date date=sdf.parse(dataStr);
                rssItem.setPubDate(date);

                String discription=cursor.getString(cursor.
                        getColumnIndex("description"));
                rssItem.setDescription(discription);

                String content=cursor.getString(cursor.
                        getColumnIndex("content"));
                rssItem.setContent(content);

                //rssItem.setState(0);

                rssItemsList.add(rssItem);
            } while (cursor.moveToNext());
        }
        cursor.close();


        return rssItemsList;
    }



    public int SaveRssItems(List<RssItem> rssItemsList){
        int count=0;
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (RssItem item:rssItemsList) {
            // 开始组装数据
            values.put("title",item.getTitle());
            values.put("link",item.getLink());
            String dateStr="";
            try{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
                dateStr=sdf.format(item.getPubDate());
                values.put("pubDate",dateStr);
            }catch (Exception e){
                //
            }finally {
                values.put("pubDate",dateStr);
            }
            values.put("description",item.getDescription());
            values.put("content",item.getContent());
            long reteurnTag=db.insert("RssItem", null, values); // 插入数据
            if(reteurnTag!=-1)
                count++;
            values.clear();
        }
        return count;
    }


    public void InitialSource(){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        RssSource[] sources={
                new RssSource("知乎每日精选", "https://www.zhihu.com/rss",false ),
                new RssSource("网易有态度专栏", "http://news.163.com/special/00011K6L/rss_newsattitude.xml",false ),
                new RssSource("搜狐体育", "http://rss.news.sohu.com/rss/sports.xml",false ),
                new RssSource("搜狐焦点", "http://rss.news.sohu.com/rss/focus.xml",false ),
                new RssSource("Engadget 中国版", "https://www.zhihu.com/rss",false ),
                new RssSource("爱范儿", "http://www.geekfan.net/feed",false ),
                new RssSource("丁香园", "http://www.dxy.cn/bbs/rss/2.0/all.xml",false ),
                new RssSource("国家地理中文网", "http://www.nationalgeographic.com.cn/index.php?m=content&c=feed",false ),
                new RssSource("FT中文新闻", "http://www.ftchinese.com/rss/feed",false )
        };

        for(int i=0;i<sources.length;i++){
            // 开始组装数据
            values.put("name", sources[i].getName());
            values.put("address", sources[i].getAddress());
            values.put("able",sources[i].IsChosen()? 1:0);
            db.insert("Source", null, values); // 插入数据
            values.clear();
        }
    }

    public List<RssSource> getRssSourceList(){
        SQLiteDatabase db=this.getReadableDatabase();
        List<RssSource> list=new ArrayList<>();

        Cursor cursor = db.query("Source", null, null, null, null, null, null);
        if(cursor.getCount()==0)
            InitialSource();
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
        return list;
    }

    public List<RssSource> getValidSourceList()
    {
        List<RssSource> allList=this.getRssSourceList();
        List<RssSource> validList=new ArrayList<RssSource>();
        for (RssSource source:allList) {
            if(source.IsChosen())
                validList.add(source);
        }
        return validList;

    }

    public void updateRssSourceState(List<RssSource> sourceList)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (RssSource source: sourceList) {
            values.put("able", source.IsChosen()? 1:0);
            db.update("Source", values, "name = ?", new String[] { source.getName() });
            values.clear();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        InitialSource();
    }
}
