package com.wgu.mbuel.wgumobileapp.database.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wgu.mbuel.wgumobileapp.R;
import com.wgu.mbuel.wgumobileapp.database.List.AssessmentDetailList;
import com.wgu.mbuel.wgumobileapp.database.List.CourseList;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/30/2017.
 * ListViewAdapter for Assessment List (View) Activity
 */
//DONE: IMPLEMENT
public class AssessmentListViewAdapter extends ArrayAdapter<AssessmentDetailList> {
    private Context context;
    private ArrayList<AssessmentDetailList> assessmentList;
    private static LayoutInflater inflater = null;

    public AssessmentListViewAdapter(@NonNull Context context, @LayoutRes int textViewResourceId, @NonNull ArrayList<AssessmentDetailList> assessments) {
        super(context, textViewResourceId, assessments);
        try {
            this.context = context;
            this.assessmentList = assessments;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {

        }
    }
    public int getCount(){
        return this.assessmentList.size();
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
            Log.d("ALVA","Due date: " + assessmentList.get(position).getAssessmentDetailDueDate());
            String assessmentDue = assessmentList.get(position).getAssessmentDetailTitle() + "\t DUE: " +
                    assessmentList.get(position).getAssessmentDetailDueDate();
            Log.d("ALVA","Concatenated String Due date: " + assessmentDue);
            holder.list_item.setText(assessmentDue);

        } catch (Exception e) {


        }
        return vi;
    }
}
