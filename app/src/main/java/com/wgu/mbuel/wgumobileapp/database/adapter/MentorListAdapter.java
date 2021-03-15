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
import com.wgu.mbuel.wgumobileapp.database.List.MentorList;
import com.wgu.mbuel.wgumobileapp.database.List.TermList;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/26/2017.
 * MentorListAdapter - populate ListView and Spinner with list of mentors
 */

public class MentorListAdapter extends ArrayAdapter<MentorList> {
    private Context context;
    private ArrayList<MentorList> mentorLists;
    private static LayoutInflater inflater = null;

    public MentorListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<MentorList> mentorLists) {
        super(context, textViewResourceId, mentorLists);
        try {
            this.context = context;
            this.mentorLists = mentorLists;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }
    public int getCount(){
        return mentorLists.size();
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
            holder.list_item.setText(mentorLists.get(position).getMentorName());


        } catch (Exception e) {


        }
        return vi;
    }
}
