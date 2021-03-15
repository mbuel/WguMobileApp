package com.wgu.mbuel.wgumobileapp.database.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wgu.mbuel.wgumobileapp.R;
import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.MentorAssignmentList;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by mbuel on 4/23/2017.
 * creates list of courses assigned to a mentor, to return to MentorDetail view
 */

public class MentorDetailsCourseListAdapter extends ArrayAdapter<MentorAssignmentList> implements SpinnerAdapter{
    private Context context;
    private ArrayList<MentorAssignmentList> mentorAssignmentLists;
    private static LayoutInflater inflater = null;

    public MentorDetailsCourseListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<MentorAssignmentList> mentorAssignmentList) {
        super(context, textViewResourceId, mentorAssignmentList);
        try {
            this.context = context;
            this.mentorAssignmentLists = mentorAssignmentList;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }
    public int getCount(){
        return mentorAssignmentLists.size();
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

            holder.list_item.setText(mentorAssignmentLists.get(position).getCourseTitle());


        } catch (Exception e) {


        }
        return vi;
    }

}