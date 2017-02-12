package me.blue.rss;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

/**
 * Created by blue on 2017/2/11.
 */

public class RssItemAdapter extends ArrayAdapter<RssItem> {

    private  int resourceId;

    public RssItemAdapter(Context context, int viewResourceId, List<RssItem> objects){
        super(context,viewResourceId,objects);
        resourceId=viewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RssItem item=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView title_text=(TextView) view.findViewById(R.id.rss_title_text);
        ImageButton read_button=(ImageButton) view.findViewById(R.id.read_button);
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
