package me.blue.rss;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.net.URLEncoder;

public class RssItemDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_item_details);
        Button back_button=(Button) findViewById(R.id.backButton);
        back_button.setOnClickListener(this);

        Intent intent=getIntent();
        RssItem item=(RssItem)intent.getParcelableExtra("rss_item");

        String title=intent.getStringExtra("title");
        //String source=intent.getStringExtra("source");
        String datetime=intent.getStringExtra("date_time");
        String details=intent.getStringExtra("details");
        link=intent.getStringExtra("link");

        TextView title_view=(TextView) findViewById(R.id.rss_item_title);
        title_view.setText(title);

        //TextView source_view=(TextView) findViewById(R.id.rss_source);
        //source_view.setText(source);

        TextView datetime_view=(TextView) findViewById(R.id.data_time);
        datetime_view.setText(datetime);

        WebView content_view=(WebView) findViewById(R.id.rss_item_content);

        WebSettings ws = content_view.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        content_view.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        content_view.loadData(details, "text/html; charset=UTF-8", null);//这种写法可以正确解码

        TextView more_link=(TextView) findViewById(R.id.rss_link);
        more_link.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                finish();
            break;
            case R.id.rss_link:
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(this.link));
                startActivity(intent);
            break;
            default:
                break;
        }
    }
}
