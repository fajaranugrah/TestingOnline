package com.example.fajar.testingonline;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fajar on 02/11/2016.
 */

public class SwipeListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<statistic> movieList;
    private String[] bgColors;

    public SwipeListAdapter(Activity activity, List<statistic> movieList) {
        this.activity = activity;
        this.movieList = movieList;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.list_bg);
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int location) {
        return movieList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        //TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView email_ans = (TextView) convertView.findViewById(R.id.email_ans);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        //serial.setText(String.valueOf(movieList.get(position).id));
        email_ans.setText(movieList.get(position).email_ans);
        name.setText(movieList.get(position).name);

        //String color = bgColors[position % bgColors.length];
        //serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }
}
