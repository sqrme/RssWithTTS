package me.blue.rss;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by blue on 2017/2/11.
 */

public class RssItemAdapter extends ArrayAdapter<RssItem> {

    private  int resourceId;
    final private  List<RssItem> rssItemList;
    private  TTSProcesser ttsProcesser;
    private RssItemAdapter _adapter;

    public RssItemAdapter(Context context, int viewResourceId, List<RssItem> objects,TTSProcesser ttsProcesser){
        super(context,viewResourceId,objects);
        resourceId=viewResourceId;
        this.ttsProcesser =ttsProcesser;
        this.rssItemList=objects;
        _adapter=this;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final RssItem item=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        final TextView title_text=(TextView) view.findViewById(R.id.rss_title_text);
        ImageButton read_button=(ImageButton) view.findViewById(R.id.read_button);
        read_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String rawText=TextTransformer.getTextFromHtml(item.getDescription());

                if(!ttsProcesser.getFinishState()){
                    ttsProcesser.StopCurrentSpeech();
                }
                /*ttsProcesser.Speek(_adapter,position);*/
                for(int i=position;i<_adapter.getCount();i++){
                    ListView listView=(ListView)(parent);
                    //View currentView=listView.getChildAt(i);
                    //TextView textView=(TextView)currentView.findViewById(R.id.rss_title_text);
                    //textView.setTextColor(getContext().getResources().getColor(R.color.colorMarked));
                    String rawText=TextTransformer.getTextFromHtml(getItem(i).getDescription());
                    ttsProcesser.Speek(rawText);
                    if(i<getCount()-1)
                        ttsProcesser.Speek("下一条");
                    else
                        ttsProcesser.Speek("消息播报结束");
                }
            }
        });

        read_button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ttsProcesser.StopCurrentSpeech();
                return true;
            }
        });

        title_text.setText(item.getTitle());
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getContext(),RssItemDetailsActivity.class);
                try {
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("source", item.getFeed().getTitle());
                    intent.putExtra("details", item.getDescription());
                    intent.putExtra("link",item.getLink());
                    intent.putExtra("date_time", item.getPubDate().toString());
                    Log.d("content",item.getContent());
                }catch (Exception e)
                {
                    //
                }
                Log.d("details",item.getLink());
                getContext().startActivity(intent);
            }
        });
        return view;
    }

}
