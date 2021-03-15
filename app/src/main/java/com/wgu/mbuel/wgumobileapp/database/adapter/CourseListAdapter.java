package com.wgu.mbuel.wgumobileapp.database.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wgu.mbuel.wgumobileapp.R;
import com.wgu.mbuel.wgumobileapp.database.List.CourseList;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/23/2017.
 * Display Course List Adapter - for ListView in Activity
 */

public class CourseListAdapter extends ArrayAdapter<CourseList> {
    private Context context;
    private ArrayList<CourseList> courseLists;
    private static LayoutInflater inflater = null;

    public CourseListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<CourseList> courseLists) {
        super(context, textViewResourceId, courseLists);
        try {
            this.context = context;
            this.courseLists = courseLists;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }
    public int getCount(){
        return courseLists.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView list_item;

    }

    @Override
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

            holder.list_item.setText(courseLists.get(position).getCourseTitle());

        } catch (Exception e) {


        }
        return vi;
    }
}
