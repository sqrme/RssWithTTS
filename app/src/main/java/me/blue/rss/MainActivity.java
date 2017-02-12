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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener {

    private List<RssSource> validSourceList=new ArrayList<RssSource>();
    private RssDatabaseHelper dbHelper;
    private int MaxCount=20;


    private List<RssFeed> FeedList=new ArrayList<RssFeed>();

    private  String title;
    URL url = null;// new URL("http://feeds.bbci.co.uk/news/world/rss.xml");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //软件activity的布局
        Button menuButton =(Button) findViewById(R.id.refreshButton);
        menuButton.setOnClickListener(this);

        dbHelper = new RssDatabaseHelper(this, "myrss.db", null, 1);

        Button refreshButton =(Button) findViewById(R.id.menuButton);
        refreshButton.setOnClickListener(this);

        /*
        new Thread(networkTask).start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView txt=(TextView)findViewById(R.id.newsText);
        txt.setText(title);*/
    }

    private class NetworkDataLoader extends AsyncTask<List<RssSource>,Integer,List<RssItem>>{

        private String [] sou_string;

        @Override
        protected void onPreExecute() {
            Log.d("main","start.......");
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            ProgressBar progressBar=(ProgressBar) findViewById(R.id.progresBar);
            progressBar.setProgress(values[0]);
            if(values[1]==0)
                Toast.makeText(MainActivity.this,sou_string[values[0]-1] +"载入失败！", Toast.LENGTH_SHORT).show();
        }


        @Override
        protected List<RssItem> doInBackground(List<RssSource>... lists) {
            List<RssItem> rssItemsList=new ArrayList<RssItem>();
            int proress=0;
            sou_string=new String[lists[0].size()];
            int i=0;
            for (RssSource source:lists[0]) {
                sou_string[proress]=source.getName();
                proress++;
                Log.d("main",String.valueOf(lists[0].size()));
                try {
                    URL url = new URL(source.getAddress());
                    RssFeed feed = RssReader.read(url);

                ArrayList<RssItem> rssItems = feed.getRssItems();
                for (RssItem rssItem : rssItems) {
                    Log.d("main", rssItem.getTitle());
                    i++;
                    //loadCount[]
                    if(i==MaxCount)
                        break;
                    rssItemsList.add(rssItem);
                }
                } catch (MalformedURLException e) {
                    Log.d("RSS Reader", e.toString());
                    e.printStackTrace();

                } catch (SAXException e) {
                    Log.d("RSS Reader", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("RSS Reader", e.toString());
                    e.printStackTrace();
                }
                finally {
                    publishProgress(proress,i);
                }
        }
            return rssItemsList;
        }

        @Override
        protected void onPostExecute(List<RssItem> rssItemList){
            RssItemAdapter rssItemAdapter=new RssItemAdapter(MainActivity.this,R.layout.rss_item,rssItemList);
            ListView listView=(ListView) findViewById(R.id.rss_item_list_view);
            listView.setAdapter(rssItemAdapter);
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

    private void loadNewsFromDatabse(){
        //
    }

    private void loadNewsFromWeb(){
        for (RssSource source: validSourceList) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refreshButton:
                //Toast toast=Toast.makeText(this.getApplicationContext(),"refreshing",Toast.LENGTH_SHORT).show();;
                validSourceList=dbHelper.getValidSourceList();
                NetworkDataLoader loader=new NetworkDataLoader();
                loader.execute(validSourceList);
                ProgressBar progressBar=(ProgressBar) findViewById(R.id.progresBar);
                progressBar.setMax(validSourceList.size());

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
