package me.blue.rss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SubManagerActivity extends AppCompatActivity {

    private List<RssSource> sourceList=new ArrayList<RssSource>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_source);
        initRssSourceList();
        Log.d("RSS","sqr");
        Log.d("RSS",String.valueOf(sourceList.size()));
        RssSourceAdapter rssSourceAdapter=new RssSourceAdapter(SubManagerActivity.this,R.layout.source_item,sourceList);
        ListView listView=(ListView) findViewById(R.id.source_view);
        listView.setAdapter(rssSourceAdapter);
    }

    private void initRssSourceList()
    {
        sourceList.add(new RssSource("BBC UK","http://feeds.bbci.co.uk/news/world/rss.xml",true));
        sourceList.add(new RssSource("知乎每日精选","https://www.zhihu.com/rss",true));
        sourceList.add(new RssSource("果壳网","http://www.guokr.com/rss",true));
        sourceList.add(new RssSource("科学松鼠会","http://songshuhui.net/feed",true));
        sourceList.add(new RssSource("南都周刊","http://www.nbweekly.com/rss/smw/",true));
    }
}
