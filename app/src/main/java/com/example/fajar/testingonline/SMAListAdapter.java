package com.example.fajar.testingonline;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fajar on 23/01/2017.
 */

public class SMAListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<sma> SMAList;
    private List<sma> originalList;
    private String[] bgColors;
    private ItemFilter filter = new ItemFilter();

    public SMAListAdapter(Activity activity, List<sma> SMAList) {
        this.activity = activity;
        this.SMAList = SMAList;
        this.originalList = new ArrayList<sma>(SMAList);
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.list_bg);
    }

    //Add Below Method
    public void reloadData(){
        this.originalList = new ArrayList<sma>(SMAList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return SMAList.size();
    }

    @Override
    public Object getItem(int location) {
        return SMAList.get(location);
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
            convertView = inflater.inflate(R.layout.list_sma, null);

        //TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView email_mak = (TextView) convertView.findViewById(R.id.email_mak);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);

        //serial.setText(String.valueOf(movieList.get(position).id));
        email_mak.setText(SMAList.get(position).email_mak);
        name.setText(SMAList.get(position).name);
        tanggal.setText(SMAList.get(position).tanggal);
        String jud = SMAList.get(position).judul;
        judul.setText(jud.toString().replace("_", " "));


        //String color = bgColors[position % bgColors.length];
        //serial.setBackgroundColor(Color.parseColor(color));

        return convertView;
    }

    public Filter getFilter(){
        return filter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint){

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<sma> list = originalList;

            int count = list.size();
            final ArrayList<sma> nlist = new ArrayList<sma>(count);

            String filterableString ;
            for (sma quiz : originalList) {
                if (quiz.judul.toLowerCase().contains(filterString)) {
                    nlist.add(quiz);
                } else if (quiz.email_mak.toLowerCase().contains(filterString)){
                    nlist.add(quiz);
                }else if (quiz.name.toLowerCase().contains(filterString)){
                    nlist.add(quiz);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return  results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            SMAList.clear();
            SMAList.addAll((ArrayList<sma>) results.values);
            notifyDataSetChanged();
        }
    }
}
