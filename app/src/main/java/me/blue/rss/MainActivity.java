package me.blue.rss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , PopupMenu.OnMenuItemClickListener {

    private  String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //软件activity的布局
        Button menuButton=(Button) findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this);


        /*new Thread(networkTask).start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView txt=(TextView)findViewById(R.id.newsText);
        txt.setText(title);*/
    }




    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            URL url = null;
            try {
                url = new URL("http://feeds.bbci.co.uk/news/world/rss.xml");
                RssFeed feed = RssReader.read(url);
                title=feed.getTitle();
                ArrayList<RssItem> rssItems = feed.getRssItems();
                for (RssItem rssItem : rssItems) {
                    //Log.i("RSS Reader", rssItem.getTitle());
                    title+="\n"+ rssItem.getTitle();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        TextView txt=(TextView)findViewById(R.id.newsText);
        switch (menuItem.getItemId()){
            case R.id.subscribe:
                Intent intent=new Intent(MainActivity.this,SubManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                txt.setText("配置理布局");
                break;
            case R.id.help:
                txt.setText("帮助布局");
                break;
            case R.id.ref:
                txt.setText("反馈信息布局");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
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
