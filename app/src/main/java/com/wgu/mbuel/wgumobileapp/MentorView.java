package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.MentorList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.MentorListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorAssignmentsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorRepo;

import java.util.ArrayList;

public class MentorView
        extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener  {

    public static final int REQUEST_CODE= 1005;
    private final String CLASS_NAME = this.getClass().getName();

    //database
    private MentorRepo mentorRepo = new MentorRepo();
    private MentorAssignmentsRepo mentorAssignmentsRepo = new MentorAssignmentsRepo();

    //Lists
    private ArrayList<MentorList> mentorLists;
    private MentorListAdapter mentorListAdapter;

    private Intent setIntent;
    private CommonFunctions commonFunctions = new CommonFunctions();

    private ListView mentorListView;

    /**
     * OnCreate - main method that instantiates the MentorView.
     * @param savedInstanceState passed bundle value
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Mentor List");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMentorList);
        fab.setOnClickListener(this);

        this.mentorLists = mentorRepo.readMentorNameList();

        //If there are currently no mentors, do not create or populate the MentorListView.
        Log.d(CLASS_NAME,"Mentor list is empty? " + this.mentorLists.isEmpty());
        if (!(this.mentorLists.isEmpty())) {
            refreshMentorList();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * used to refresh list after deletion
     */
    private void refreshMentorList()
    {
        this.mentorLists = mentorRepo.readMentorNameList();
        mentorListAdapter = new MentorListAdapter(App.getContext(), 0, mentorLists);
        mentorListAdapter.notifyDataSetChanged();

        mentorListView = (ListView) findViewById(R.id.listMentor);
        mentorListView.setAdapter(mentorListAdapter);
        mentorListView.setOnItemClickListener(this);
        mentorListView.setOnItemLongClickListener(this);
    }
    /**
     * OnClick method, used to keep all click events in one place.
     * @param v - View item that was clicked on.
     */
    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.fabMentorList)){
            Snackbar.make(v, "Creating new term", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            this.setIntent = new Intent(MentorView.this,MentorSelection.class);
            MentorList newMentorList = new MentorList();

            setIntentLaunchActivity(newMentorList);

        }
    }

    /**
     * onItemClick - Used for ListView click events. Determines which item was clicked on and passes that to the next class
     * @param parent this class
     * @param view ListView object
     * @param position position
     * @param id id of listView position
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(CLASS_NAME,"View value: " + view);
        Log.d(CLASS_NAME,"R.id.listMentor: " + R.id.listMentor);
        Log.d(CLASS_NAME,"Selected position: " + position);
        Log.d(CLASS_NAME,"Selected id: " + id);

        MentorList selectedMentor = (MentorList) this.mentorListView.getItemAtPosition(position);

        this.setIntent = new Intent(MentorView.this,MentorSelection.class);

        setIntentLaunchActivity(selectedMentor);
    }

    /**
     * OnItemLongClick - used for ListView to delete an item without opening detail view
     * @param parent this class
     * @param view ListView object
     * @param position position
     * @param id id of listView position
     * @return boolean value of delete.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final MentorList selectedMentor = (MentorList) this.mentorListView.getItemAtPosition(position);
        int deletedRow = 0;
        Mentor updatedMentorValues = updateMentorDetails(selectedMentor);
        if (commonFunctions.tryParseInt(updatedMentorValues.getMentorId())  && commonFunctions.getInteger() > 0) {

            if (!(mentorAssignmentsRepo.readMentorAssignmentExists(updatedMentorValues))) {
                deletedRow = mentorRepo.delete(updatedMentorValues);
                commonFunctions.makeToast("Mentor: " + updatedMentorValues.getMentorName() +  " deleted from table.");
                refreshMentorList();
            }
            else
            {
                commonFunctions.makeToast("Unable to delete Mentor with courses assigned.");
            }
        }


        return deletedRow > 0; //If mentor Id has been set to zero upon delete, return that delete has been successful.
    }

    /**
     * updateMentorDetails - converts selected MentorList into a Mentor object
     * @param selectedMentor - selected mentor in long click
     * @return Mentor object
     */
    private Mentor updateMentorDetails(MentorList selectedMentor)
    {
        Mentor updatedMentorDetails = new Mentor();
        updatedMentorDetails.setMentorId(selectedMentor.getMentorId());
        updatedMentorDetails.setMentorName(selectedMentor.getMentorName());
        updatedMentorDetails.setMentorHomePhoneNumber(selectedMentor.getMentorHomePhoneNumber());
        updatedMentorDetails.setMentorCellPhoneNumber(selectedMentor.getMentorCellPhoneNumber());
        updatedMentorDetails.setMentorHomeEmail(selectedMentor.getMentorHomeEmail());
        updatedMentorDetails.setMentorWorkEmail(selectedMentor.getMentorWorkEmail());

        return updatedMentorDetails;
    }

    /**
     * setIntentLaunchActivity
     * Bundles values that will be passed to the next Activity.
     * @param mentorList MentorList from selected Mentor
     */
    private void setIntentLaunchActivity(MentorList mentorList)
    {
        setIntent.putExtra(Mentor.MENTOR_ID, mentorList.getMentorId());
        setIntent.putExtra(Mentor.MENTOR_NAME, mentorList.getMentorName());
        setIntent.putExtra(Mentor.MENTOR_HOME_PHONE_NUMBER,mentorList.getMentorHomePhoneNumber());
        setIntent.putExtra(Mentor.MENTOR_CELL_PHONE_NUMBER,mentorList.getMentorCellPhoneNumber());
        setIntent.putExtra(Mentor.MENTOR_HOME_EMAIL,mentorList.getMentorHomeEmail());
        setIntent.putExtra(Mentor.MENTOR_WORK_EMAIL,mentorList.getMentorWorkEmail());
        setIntent.putExtra(Mentor.MENTOR_CREATED, mentorList.getMentorCreated());

        startActivityForResult(this.setIntent,REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MentorSelection.REQUEST_CODE && resultCode == RESULT_OK)
        {
            Log.d(CLASS_NAME,"Results: " + data.getCategories());
        }
        refreshMentorList();
    }



}
