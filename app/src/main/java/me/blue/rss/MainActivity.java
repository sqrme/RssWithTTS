package me.blue.rss;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener {

    private List<RssSource> validSourceList=new ArrayList<RssSource>();
    private RssDatabaseHelper dbHelper;
    private int MaxCount=20;
    private TTSProcesser ttsProcesser;
    private ListView listView;

    private List<RssItem> rssItemsList=new ArrayList<RssItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //软件activity的布局
        Button menuButton =(Button) findViewById(R.id.refreshButton);
        menuButton.setOnClickListener(this);

        //listView=(ListView) findViewById(R.id.rss_item_list_view);

        dbHelper = new RssDatabaseHelper(this, "myrss.db", null, 1);

        Button refreshButton =(Button) findViewById(R.id.menuButton);
        ttsProcesser = new TTSProcesser(this);
        refreshButton.setOnClickListener(this);
    }

    private class NetworkDataLoader extends AsyncTask<List<RssSource>,Integer,List<RssItem>>{

        private String [] sou_string;

        @Override
        protected void onPreExecute(){
            AppLog.d("main","start.......");
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            ProgressBar progressBar=(ProgressBar) findViewById(R.id.progresBar);
            progressBar.setProgress(values[0]);
            if(values[1]==0)
                Toast.makeText(MyApplication.getContext(),sou_string[values[0]-1] +"载入失败！", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected List<RssItem> doInBackground(List<RssSource>... lists) {
            List<RssItem> rssItemList=new ArrayList<RssItem>();
            //rssItemsList;
            int proress=0;
            sou_string=new String[lists[0].size()];
            int i=0;
            for (RssSource source:lists[0]) {
                sou_string[proress]=source.getName();
                proress++;
                AppLog.d("main",String.valueOf(lists[0].size()));
                try {
                    URL url = new URL(source.getAddress());
                    RssFeed feed = RssReader.read(url);

                ArrayList<RssItem> rssItems = feed.getRssItems();
                for (RssItem rssItem : rssItems) {
                    AppLog.d("main", rssItem.getTitle());
                    i++;
                    //loadCount[]
                    if(i==MaxCount)
                        break;
                    rssItemList.add(rssItem);
                }
                } catch (MalformedURLException e) {
                    AppLog.d("RSS Reader", e.toString());
                    e.printStackTrace();

                } catch (SAXException e) {
                    AppLog.d("RSS Reader", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    AppLog.d("RSS Reader", e.toString());
                    e.printStackTrace();
                }
                finally {
                    publishProgress(proress,i);
                }
        }
            return rssItemList;
        }

        @Override
        protected void onPostExecute(List<RssItem> rss_item_list){
            ProgressBar progressBar=(ProgressBar) findViewById(R.id.progresBar);
            //更新完毕后，隐藏进度条
            progressBar.setVisibility(View.GONE);
            if(rss_item_list.size()>0){
                rssItemsList=rss_item_list;
                saveRssItems();
                RssItemAdapter rssItemAdapter=new RssItemAdapter(MainActivity.this,R.layout.rss_item,rssItemsList,ttsProcesser);
                ListView listView=(ListView) findViewById(R.id.rss_item_list_view);
                listView.setAdapter(rssItemAdapter);
            }else
            {
                loadNewsFromDatabse();
            }

        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        //TextView txt=(TextView)findViewById(R.id.newsText);
        switch (menuItem.getItemId()){
            case R.id.subscribe:
                Intent intent=new Intent(MainActivity.this,SubManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                //txt.setText("配置理布局");
                break;
            case R.id.help:
                //txt.setText("帮助布局");
                break;
            case R.id.ref:
                //txt.setText("反馈信息布局");
                break;
            default:
                break;
        }
        return true;
    }

    //// TODO: 2017/3/15 建立一个后台线程，从数据库中载入数据，以提高界面的响应效率。 
    private void loadNewsFromDatabse(){
        try {
            rssItemsList=dbHelper.LoadRssItems();
            AppLog.d("Rss",String.valueOf(rssItemsList.size()));
            RssItemAdapter rssItemAdapter=new RssItemAdapter(MainActivity.this,R.layout.rss_item,rssItemsList,ttsProcesser);
            ListView listView=(ListView) findViewById(R.id.rss_item_list_view);
            listView.setAdapter(rssItemAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void saveRssItems(){
        dbHelper.ClearRssItems();
        int saveCount=dbHelper.SaveRssItems(rssItemsList);
        if(saveCount==rssItemsList.size()){
            AppLog.d("RSS Reader","Clear Rss Items sucess!");
        }
    }


    private void ReloadRssItems() {

        if(MyApplication.isNetworkConnected(getApplicationContext())){
            validSourceList=dbHelper.getValidSourceList();
            ProgressBar progressBar=(ProgressBar) findViewById(R.id.progresBar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(validSourceList.size());
            progressBar.setProgress(0);
            NetworkDataLoader loader=new NetworkDataLoader();
            loader.execute(validSourceList);
        }else
        {
            if(this.rssItemsList.isEmpty()){
                loadNewsFromDatabse();
            }
            Toast.makeText(getApplicationContext(),"好像没有联网哦，暂不能刷新，再试试吧！",Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refreshButton:
                ReloadRssItems();
                break;
            case R.id.menuButton:
                PopupMenu popupMenu=new PopupMenu(this,view);
                //获取菜单填充器
                MenuInflater inflater = popupMenu.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.main, popupMenu.getMenu());
                //绑定菜单项的点击事件
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
        }
    }

}