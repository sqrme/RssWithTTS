package me.blue.rss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.net.URLEncoder;

public class RssItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    final String mimeType = "text/html";
    final String encoding = "utf-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_item_details);
        Button back_button=(Button) findViewById(R.id.backButton);
        back_button.setOnClickListener(this);
        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String source=intent.getStringExtra("source");
        String datetime=intent.getStringExtra("date_time");
        String content=intent.getStringExtra("details");
        String link=intent.getStringExtra("link");

        TextView title_view=(TextView) findViewById(R.id.rss_item_title);
        title_view.setText(title);

        TextView source_view=(TextView) findViewById(R.id.rss_source);
        source_view.setText(source);

        TextView datetime_view=(TextView) findViewById(R.id.data_time);
        datetime_view.setText(datetime);

        WebView content_view=(WebView) findViewById(R.id.rss_item_content);
        //content_view.setMovementMethod(new ScrollingMovementMethod());
        content_view.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
//        webView.loadData(data, "text/html", "UTF -8");//API提供的标准用法，无法解决乱码问题
        content_view.loadData(content, "text/html; charset=UTF-8", null);//这种写法可以正确解码
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
