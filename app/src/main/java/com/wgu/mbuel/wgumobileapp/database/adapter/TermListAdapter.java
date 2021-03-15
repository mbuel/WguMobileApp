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
import com.wgu.mbuel.wgumobileapp.database.List.TermList;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/21/2017.
 * TermListAdapter - populates ListView with list of Terms from table.
 */

public class TermListAdapter extends ArrayAdapter<TermList> {
    private Context context;
    private ArrayList<TermList> termList;
    private static LayoutInflater inflater = null;

    public TermListAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<TermList> termList) {
        super(context, textViewResourceId, termList);
        try {
            this.context = context;
            this.termList = termList;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }
    public int getCount(){
        return termList.size();
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
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.list_item = (TextView) vi.findViewById(R.id.tvListItem);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            String termDue = termList.get(position).getTermTitle() + "\t ENDS: " + termList.get(position).getTermEnd();
            holder.list_item.setText(termDue);

        } catch (Exception e) {


        }
        return vi;
    }
}
