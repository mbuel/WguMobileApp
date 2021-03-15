package com.wgu.mbuel.wgumobileapp.database.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wgu.mbuel.wgumobileapp.R;
import com.wgu.mbuel.wgumobileapp.database.List.MentorAssignmentList;
import com.wgu.mbuel.wgumobileapp.database.List.MentorList;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/26/2017.
 * CourseDetails List Adapter - shows the connection of Mentors assigned to Courses.
 */

public class CourseDetailsMentorListAdapter extends ArrayAdapter<MentorAssignmentList> implements SpinnerAdapter{
    private Context context;
    private ArrayList<MentorAssignmentList> mentorAssignmentLists;
    private static LayoutInflater inflater = null;

    public CourseDetailsMentorListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<MentorAssignmentList> mentorDetailList) {
        super(context, textViewResourceId, mentorDetailList);
        try {
            this.context = context;
            this.mentorAssignmentLists = mentorDetailList;

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

            holder.list_item.setText(mentorAssignmentLists.get(position).getMentorName());


        } catch (Exception e) {


        }
        return vi;
    }
}