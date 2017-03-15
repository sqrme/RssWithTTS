package me.blue.rss;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.button;
import static android.R.attr.onClick;
import static java.security.AccessController.getContext;

public class SubManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private List<RssSource> sourceList=new ArrayList<RssSource>();
    RssDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_source);
        dbHelper = new RssDatabaseHelper(this, "myrss.db", null, 1);
        InitialSourceList();
        InitialSourceList();
        Button backButton=(Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    private void InitialSourceList()
    {
        sourceList=dbHelper.getRssSourceList();
        Log.d("RSS","sqr");
        Log.d("RSS",String.valueOf(sourceList.size()));
        RssSourceAdapter rssSourceAdapter=new RssSourceAdapter(SubManagerActivity.this,R.layout.source_item,sourceList);
        ListView listView=(ListView) findViewById(R.id.source_view);
        listView.setAdapter(rssSourceAdapter);

    }


    @Override
    public void onClick(View view){
        dbHelper.updateRssSourceState(this.sourceList);
        this.finish();;
    }

}
