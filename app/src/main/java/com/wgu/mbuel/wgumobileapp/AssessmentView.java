package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.AssessmentDetailList;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.adapter.AssessmentListViewAdapter;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.model.*;
import com.wgu.mbuel.wgumobileapp.database.repo.AssessmentDetailRepo;

import java.util.ArrayList;

public class AssessmentView
        extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener {
    public static final int REQUEST_CODE= 1009;
    private static final String ASSESSMENT_VIEW = "assessmentView";
    private final String CLASS_NAME = this.getClass().getName();

    private Intent setIntent;
    private Intent getIntent;
    private Bundle getBundle;

    private CommonFunctions commonFunctions = new CommonFunctions();

    private TermDetailList termDetailCourseAssigned = new TermDetailList();
    private AssessmentListViewAdapter assessmentViewAdapter;
    private AssessmentDetailRepo adRepo = new AssessmentDetailRepo();
    private AssessmentDetailList assessmentDetail;
    private ArrayList<AssessmentDetailList> listAssessments;

    private ListView listViewAssessments;
    private EditText termTitle, courseTitle;


    /**
     * onCreate - Loads values for Assessment List
     * @param savedInstanceState - bundle of data passed from prior activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Assessment List");
        this.getIntent = getIntent();
        this.getBundle = this.getIntent.getExtras();


        this.termTitle = (EditText) findViewById(R.id.editTextAssessmentViewTermTitle);
        this.courseTitle = (EditText) findViewById(R.id.editTextAssessmentViewCourseTitle);

        Log.d(CLASS_NAME,"this.getBundle == null? " + (this.getBundle == null));

        if (this.getBundle != null)
        {
            this.termDetailCourseAssigned.setCourseDetailsId(this.getBundle.getString(CourseDetails.COURSE_DETAILS_ID));
            this.termTitle.setText(this.getBundle.getString(CourseDetails.TERM_TITLE));
            this.courseTitle.setText(this.getBundle.getString(CourseDetails.COURSE_TITLE));
            this.termDetailCourseAssigned.setTermTitle(this.termTitle.getText().toString());
            this.termDetailCourseAssigned.setCourseTitle(this.courseTitle.getText().toString());
        }
        else
        {
            restorePrefs();
            this.termTitle.setText(this.termDetailCourseAssigned.getTermTitle());
            this.courseTitle.setText(this.termDetailCourseAssigned.getCourseTitle());
        }

        this.listAssessments = adRepo.readAssessmentDetailList(this.termDetailCourseAssigned);
        if (!this.listAssessments.isEmpty())
        {
            refreshListView();
        }

        FloatingActionButton fabAssessmentList = (FloatingActionButton) findViewById(R.id.fabAssessmentList);
        fabAssessmentList.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * refreshListView - creates and refreshes the list view.
     */
    private void refreshListView() {
        this.listAssessments = adRepo.readAssessmentDetailList(this.termDetailCourseAssigned);
        this.assessmentViewAdapter = new AssessmentListViewAdapter(App.getContext(),0,this.listAssessments);
        this.assessmentViewAdapter.notifyDataSetChanged();

        this.listViewAssessments = (ListView) findViewById(R.id.lvAlAssessmentList);
        this.listViewAssessments.setAdapter(this.assessmentViewAdapter);
        this.listViewAssessments.setOnItemClickListener(this);
        this.listViewAssessments.setOnItemLongClickListener(this);
    }

    /**
     * click events, only handles the FAB button to create a new assessment detail
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.fabAssessmentList)){
            Snackbar.make(v, "Creating new Assessment", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            writePrefs();
            this.setIntent = new Intent(AssessmentView.this, AssessmentSelection.class);
            AssessmentDetailList emptyAssessmentList = new AssessmentDetailList();
            emptyAssessmentList.setAssessmentDetailCourseDetailId(this.termDetailCourseAssigned.getCourseDetailsId());
            Log.d(CLASS_NAME,"CourseDetailsId: " + emptyAssessmentList.getAssessmentDetailCourseDetailId());

            setIntentLaunchActivity(emptyAssessmentList);

        }
    }

    /**
     * used to bundle data and open AssessmentSelection.class
     * @param assessmentSelection
     */
    private void setIntentLaunchActivity(AssessmentDetailList assessmentSelection) {
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_ID,assessmentSelection.getAssessmentDetailId());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_ALARM,assessmentSelection.getAssessmentDetailAlarm());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID,assessmentSelection.getAssessmentDetailCourseDetailId());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE,assessmentSelection.getAssessmentDetailCompletionDate());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_CREATED,assessmentSelection.getAssessmentDetailCreated());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE,assessmentSelection.getAssessmentDetailDueDate());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_NOTES,assessmentSelection.getAssessmentDetailNotes());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH,assessmentSelection.getAssessmentDetailPhotoPath());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_STATUS,assessmentSelection.getAssessmentDetailStatus());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_TITLE,assessmentSelection.getAssessmentDetailTitle());
        this.setIntent.putExtra(AssessmentDetails.ASSESSMENT_DETAIL_TYPE,assessmentSelection.getAssessmentDetailType());

        startActivityForResult(this.setIntent,REQUEST_CODE);
    }

    /**
     * onItemClick- used when you select an item on the list
     * @param parent - Activity
     * @param view - ListView
     * @param position - Position clicked
     * @param id - id of position
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(CLASS_NAME,"View value: " + view);
        Log.d(CLASS_NAME,"R.id.listMentor: " + R.id.lvAlAssessmentList);
        Log.d(CLASS_NAME,"Selected position: " + position);
        Log.d(CLASS_NAME,"Selected id: " + id);

        this.setIntent = new Intent(AssessmentView.this,AssessmentSelection.class);

        AssessmentDetailList selectedAssessment = (AssessmentDetailList) this.listViewAssessments.getItemAtPosition(position);
        Log.d(CLASS_NAME,"Assessment ID:" + selectedAssessment.getAssessmentDetailId());

        setIntentLaunchActivity(selectedAssessment);
    }

    /**
     * onItemLongClick - used to delete items from the list.
     * @param parent - Activity
     * @param view - ListView
     * @param position - Position to delete
     * @param id - id of Position
     * @return true/false for status of delete.
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(CLASS_NAME,"Delete Assessment.");
        AssessmentDetailList assessmentToRemove = (AssessmentDetailList) this.listViewAssessments.getItemAtPosition(position);


        if (adRepo.delete(assessmentToRemove) > 0)
        {
            commonFunctions.makeToast("Assessment assignment deleted.");
            refreshListView();
            return true;
        }
        return false;
    }

    /**
     * writePrefs - write SharedPrefs to the storage
     */
    private void writePrefs()
    {
        SharedPreferences.Editor setAssessViewPref = getSharedPreferences(ASSESSMENT_VIEW, MODE_PRIVATE).edit();

        setAssessViewPref.putString(CourseDetails.TERM_TITLE,this.termDetailCourseAssigned.getTermTitle());
        setAssessViewPref.putString(CourseDetails.COURSE_TITLE,this.termDetailCourseAssigned.getCourseTitle());
        setAssessViewPref.putString(CourseDetails.COURSE_DETAILS_ID,this.termDetailCourseAssigned.getCourseDetailsId());

        setAssessViewPref.apply();
    }

    /**
     * makes sure list is populated with results from AssessmentSelection.Class
     * @param requestCode code of calling activity
     * @param resultCode result code from activity
     * @param data data object from activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(CLASS_NAME,"RETURN FROM ASSESSMENT SELECTION.");
        restorePrefs();
    }

    /**
     * restorePrefs - restore SharedPrefs from storage
     */
    private void restorePrefs() {
        Log.d(CLASS_NAME,"Loading Prefs.");
        SharedPreferences getAssessViewPref = getSharedPreferences(ASSESSMENT_VIEW, MODE_PRIVATE);

        this.termDetailCourseAssigned.setCourseDetailsId(getAssessViewPref.getString(CourseDetails.COURSE_DETAILS_ID,this.termDetailCourseAssigned.getCourseDetailsCourseId()));
        Log.d(CLASS_NAME,"Course Details ID: " + this.termDetailCourseAssigned.getCourseDetailsId());
        this.termDetailCourseAssigned.setTermTitle(getAssessViewPref.getString(CourseDetails.TERM_TITLE,this.termDetailCourseAssigned.getTermTitle()));
        this.termDetailCourseAssigned.setCourseTitle(getAssessViewPref.getString(CourseDetails.COURSE_TITLE,this.termDetailCourseAssigned.getCourseTitle()));
        refreshListView();
    }
}
