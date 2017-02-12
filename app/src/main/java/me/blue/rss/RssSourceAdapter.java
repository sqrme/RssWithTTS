package me.blue.rss;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by blue on 2017/2/8.
 */

public class RssSourceAdapter extends ArrayAdapter<RssSource> {

    private  int resourceId;

    public RssSourceAdapter(Context context, int viewResourceId, List<RssSource> objects){
        super(context,viewResourceId,objects);
        resourceId=viewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RssSource source=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView sou_text=(TextView) view.findViewById(R.id.rss_name);
        CheckBox sou_check=(CheckBox) view.findViewById(R.id.rss_check);
        sou_text.setText(source.getName());
        sou_check.setChecked(source.IsChosen());
        sou_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked)
                    source.setChosen();
                else
                    source.setUnChosen();
            }
        });
        return view;
    }
}
