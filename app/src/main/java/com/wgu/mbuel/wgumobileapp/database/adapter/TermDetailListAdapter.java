package com.wgu.mbuel.wgumobileapp.database.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wgu.mbuel.wgumobileapp.R;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/22/2017.
 * TermDetailListAdapter - populates ListView with list of assigned courses to Terms from Database.
 */

public class TermDetailListAdapter extends ArrayAdapter<TermDetailList> {
    private Context context;
    private ArrayList<TermDetailList> termDetail;
    private static LayoutInflater inflater = null;

    public TermDetailListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<TermDetailList> termDetailList) {
        super(context, textViewResourceId, termDetailList);
        try {
            this.context = context;
            this.termDetail = termDetailList;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }
    public int getCount(){
        return termDetail.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView list_item;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final TermListAdapter.ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item, null);
                holder = new TermListAdapter.ViewHolder();

                holder.list_item = (TextView) vi.findViewById(R.id.tvListItem);


                vi.setTag(holder);
            } else {
                holder = (TermListAdapter.ViewHolder) vi.getTag();
            }
            String courseInfo = termDetail.get(position).getCourseTitle() + "\t DUE: " + termDetail.get(position).getCourseDetailsEndDate();
            holder.list_item.setText(courseInfo);


        } catch (Exception e) {


        }
        return vi;
    }
}
