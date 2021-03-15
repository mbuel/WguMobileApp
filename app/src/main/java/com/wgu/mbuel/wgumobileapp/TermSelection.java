package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.AssessmentDetailList;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.TermDetailListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.CourseDetails;
import com.wgu.mbuel.wgumobileapp.database.model.Term;
import com.wgu.mbuel.wgumobileapp.database.repo.AssessmentDetailRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseDetailsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.TermRepo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TermSelection
        extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener {
    public static final int REQUEST_CODE= 1004;
    private static final int MENU_ITEM_SAVE=1;
    private static final int MENU_ITEM_DELETE=2;
    private int selectedTermId = 0;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Date currentDate =  new Date();


    private final String CLASS_NAME = this.getClass().getName();

    //data objects
    private Intent setIntent;
    private Bundle getTermDetails;
    private CommonFunctions commonFunctions = new CommonFunctions();

    //List Adapter
    private ArrayList<TermDetailList> termDetailLists;
    private TermDetailListAdapter termDetailAdapter;

    //Database connection
    private TermRepo termRepo = new TermRepo();
    private Term term = new Term();
    private CourseDetailsRepo courseDetailsRepo = new CourseDetailsRepo();
    private AssessmentDetailRepo assessmentDetailRepo = new AssessmentDetailRepo();

    //Activity objects
    private ListView termDetailListView;
    private EditText termTitle, termStart, termEnd;

    /**
     * onCreate - loads this activity and unbundles passed values from previous activity
     * @param savedInstanceState - passed object to unbundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.getTermDetails = intent.getExtras();

        //unbundle local term object from bundle
        this.term.setTermId(this.getTermDetails.getString(Term.TERM_ID));
        this.term.setTermTitle(this.getTermDetails.getString(Term.TERM_TITLE));
        this.term.setTermStart(this.getTermDetails.getString(Term.TERM_START));
        this.term.setTermEnd(this.getTermDetails.getString(Term.TERM_END));
        this.term.setTermCreated(this.getTermDetails.getString(Term.TERM_CREATED));

        this.termTitle = (EditText) findViewById(R.id.editTextTermTitle);
        this.termTitle.setText(this.term.getTermTitle());

        this.termStart = (EditText) findViewById(R.id.editTextTermStartDate);
        if (this.term.getTermId().equals("0"))
        {
            this.termStart.setText(this.sdf.format(this.currentDate));
            setTitle("New Term");
        }
        else {
            this.termStart.setText(this.term.getTermStart());
            setTitle(this.term.getTermTitle() + " DETAIL");
        }
        this.termEnd = (EditText) findViewById(R.id.editTextTermEndDate);
        this.termEnd.setText(this.term.getTermEnd());

        this.selectedTermId = commonFunctions.tryParseInt(getTermDetails.getString(Term.TERM_ID))?commonFunctions.getInteger():0;

        this.termDetailLists = this.courseDetailsRepo.readTermDetailArrayList(selectedTermId);

        if (commonFunctions.tryParseInt(getTermDetails.getString(Term.TERM_ID)) && commonFunctions.getInteger() > 0) {
            Log.d(this.CLASS_NAME,"Term exists.");
            //populate list view

            if (this.termDetailLists.size() > 0)
            {
                refreshListView();
            }
        }

        FloatingActionButton fabTermDetail = (FloatingActionButton) findViewById(R.id.fabTermDetail);
        fabTermDetail.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * refreshListView when called. (on load or after delete)
     */
    private void refreshListView()
    {
//        Log.d(CLASS_NAME,"rLV - Returned term ID: " + selectedTermId);
        this.termDetailLists = this.courseDetailsRepo.readTermDetailArrayList(selectedTermId);
        this.termDetailAdapter = new TermDetailListAdapter(App.getContext(),0,this.termDetailLists);
        this.termDetailAdapter.notifyDataSetChanged();

        this.termDetailListView = (ListView) findViewById(R.id.listTermDetail);
        this.termDetailListView.setAdapter(this.termDetailAdapter);
        this.termDetailListView.setOnItemClickListener(this);
        this.termDetailListView.setOnItemLongClickListener(this);
    }

    /**
     * saveTerm - saves Term data to database
     * @return true/false for if successful saving term
     */
    private boolean saveTerm()
    {
        boolean saveResults = false;
        //Won't let Term data be saved if any fields are blank.
        if (!(this.termTitle.getText().toString().isEmpty()) && !(this.termStart.getText().toString().isEmpty()) && !(this.termStart.getText().toString().isEmpty()))
        {
            if (commonFunctions.tryParseInt(term.getTermId())  && commonFunctions.getInteger() > 0) {
                Log.d(CLASS_NAME,"Updating database.");
                if (termRepo.update(setTermValues()) > 0){
                    saveResults = true;
                }
                else
                {
                    commonFunctions.makeToast("Problem updating Term.");
                }
            }
            else
            {
                int newTermId = termRepo.create(setTermValues());
                if (newTermId > 0) {
                    Log.d(CLASS_NAME, "inserting new row in database.");

                    saveResults = true;
                    this.term.setTermId(String.valueOf(newTermId));
                    setTitle(this.term.getTermTitle() + " DETAIL");
                }
                else
                {
                    saveResults = false;
                    commonFunctions.makeToast("Problem creating entry in table for this term. Does the title already exist?");
                }
            }
            Log.d(CLASS_NAME,"Save Results Status: " + saveResults);
            commonFunctions.makeToast("Term saved.");
        }
        else
        {
            commonFunctions.makeToast("unable to save Term data with empty fields. ");
        }
        return saveResults;
    }
    /**
     * onCreateOptionsMenu - creates menu bar in top right of layout
     * @param menu menu to display
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SAVE, Menu.NONE, "save");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "delete");
        return true;
    }

    /**
     * onOptionsItemSelected - deal with click events from action bar
     * @param item item in menu
     * @return true/false from click
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SAVE:
                return saveTerm();
            case MENU_ITEM_DELETE:
                Log.d(CLASS_NAME,"termDetailLists empty? " + termDetailLists.isEmpty());
                if (termDetailLists.isEmpty()){ //Validation to make sure no courses are assigned - REQ A.3
                    termRepo.delete(setTermValues());
                    commonFunctions.makeToast("Term Deleted. ");
                    finish();
                    return true;
                }
                else
                {
                    commonFunctions.makeToast("Unable to delete term with courses assigned. ");
                    return false;
                }
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;

            //DONE: need to close form after save or delete.
        }
    }

    /**
     * makes sure Term object contains latest data from Activity
     * @return copy of Term object to manipulate
     */
    private Term setTermValues(){

        Term updatedTerm = new Term();

        updatedTerm.setTermId(term.getTermId());
        updatedTerm.setTermTitle(termTitle.getText().toString());
        this.term.setTermTitle(updatedTerm.getTermTitle());
        updatedTerm.setTermStart(termStart.getText().toString());
        this.term.setTermStart(updatedTerm.getTermStart());
        updatedTerm.setTermEnd(termEnd.getText().toString());
        this.term.setTermEnd(updatedTerm.getTermEnd());

        return updatedTerm;
    }
    /**
     * setIntentLaunchActivity - takes object and populates intent for next Activity to unpack
     * @param course - object being packed into intent bundle
     */
    private void setIntentLaunchActivity(TermDetailList course)
    {
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_TERM_ID,course.getCourseDetailsTermId());
        this.setIntent.putExtra(CourseDetails.TERM_TITLE,course.getTermTitle());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_COURSE_ID,course.getCourseDetailsCourseId());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_START_DATE,course.getCourseDetailsStartDate());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_START_ALARM,course.getCourseDetailsStartAlarm());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_END_DATE,course.getCourseDetailsEndDate());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_END_ALARM,course.getCourseDetailsEndAlarm());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_NOTES,course.getCourseDetailsNotes());
        this.setIntent.putExtra(CourseDetails.COURSE_TITLE,course.getCourseTitle());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_STATUS,course.getCourseDetailsStatus());
        this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_ID, course.getCourseDetailsId());

        startActivityForResult(this.setIntent,REQUEST_CODE);
    }

    /**
     * deleteCourseAssignment - delete any assignments that exist for this course.
     * @param courseAssignmentToDelete course to delete
     * @return true / false on whether delete was successful
     */
    private boolean deleteCourseAssessmentAssignment(TermDetailList courseAssignmentToDelete)
    {
        AssessmentDetailList assessmentDetailList = new AssessmentDetailList();
        assessmentDetailList.setAssessmentDetailCourseDetailId(courseAssignmentToDelete.getCourseDetailsId());

        int deletedRowId = 0;

        if (assessmentDetailRepo.readRowExists(assessmentDetailList))
        {
            deletedRowId = assessmentDetailRepo.delete(assessmentDetailList);
        }
        else
        {
            return true;
        }
        //DONE: code to delete term return true or false
        return deletedRowId > 0;
    }
    private boolean deleteCourseAssignment(TermDetailList courseAssignmentToDelete)
    {
        Log.d(CLASS_NAME,"DELETE COURSE DETAILS ID: " + courseAssignmentToDelete.getCourseDetailsId());
        commonFunctions.makeToast("Deleted Course: " + courseAssignmentToDelete.getCourseTitle());
        return (courseDetailsRepo.delete(courseAssignmentToDelete)) > 0;

    }
    /**
     * fab click event method - opens Course Details with empty event
     * @param v FAB view
     */
    @Override
    public void onClick(View v) {

        saveTerm();
        if (v == findViewById(R.id.fabTermDetail)){
            commonFunctions.makeToast("Adding new course to term");

            this.setIntent = new Intent(TermSelection.this,TermCourseDetail.class);
            TermDetailList emptyCourse = new TermDetailList();
            emptyCourse.setCourseDetailsTermId(this.term.getTermId());
            emptyCourse.setTermTitle(this.term.getTermTitle());

            setIntentLaunchActivity(emptyCourse);

        }
    }
    //DONE: populate this method
    /**
     * on long click, delete course from this term
     * @param parent this Activity
     * @param view - the ListView
     * @param position -position selected
     * @param id - id of position
     * @return true / false for row deleted
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        TermDetailList deleteAssignment = (TermDetailList) termDetailListView.getItemAtPosition(position);
        if (deleteCourseAssessmentAssignment(deleteAssignment))
        {
            Log.d(CLASS_NAME,"SUCCESSFUL DELETION OF ASSESSMENT ASSIGNMENT.");
            if (deleteCourseAssignment(deleteAssignment))
            {
                Log.d(CLASS_NAME,"SUCCESSFUL DELETION OF COURSE ASSIGNMENT.");
                refreshListView();
                return true;
            }
            else
            {
                Log.d(CLASS_NAME,"PROBLEM DELETING COURSE ASSIGNMENT." + "\n" +
                        "TERM ID: " + deleteAssignment.getCourseDetailsTermId() + "\n" +
                        "COURSE ID: " + deleteAssignment.getCourseDetailsCourseId() + "\n" +
                        "COURSE DETAILS ID: " + deleteAssignment.getCourseDetailsId());
                return false;
            }
        }
        else
        {
            Log.d(CLASS_NAME,"PROBLEM DELETING ASSESSMENT ASSIGNMENT FROM COURSE.");
            commonFunctions.makeToast("PROBLEM DELETING ASSESSMENT ASSIGNMENT FROM COURSE.");

        }


        return false;
    }
    /**
     * onItemCLick - load details of selected item, then pass to next activity
     * @param parent this activity
     * @param view the list view containing this item
     * @param position position of selected item
     * @param id id of selected item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //populate courseList object from selected position, pass to setIntentLaunchActivity

        //Validation to make sure Term object data is saved before loading next term.
        if (saveTerm()) {
            TermDetailList selectedCourse = (TermDetailList) termDetailListView.getItemAtPosition(position);
            Log.d(CLASS_NAME,"Course Title: " + selectedCourse.getCourseTitle());
            Log.d(CLASS_NAME,"Course ID: " + selectedCourse.getCourseDetailsCourseId());
            this.setIntent = new Intent(TermSelection.this,TermCourseDetail.class);
            selectedCourse.setTermTitle(this.termTitle.getText().toString());
            setIntentLaunchActivity(selectedCourse);
            refreshListView();
        }
        else
        {
            commonFunctions.makeToast("Unable to load course details, there was a problem saving the term data.");
        }
    }

    /**
     * onActivityResult - populate listView
     * @param requestCode calling activity
     * @param resultCode resultCode from activity
     * @param data bundled data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MentorSelection.REQUEST_CODE && resultCode == RESULT_OK)
        {
            Log.d(CLASS_NAME,"Results: " + data.getCategories());
        }
        refreshListView();
    }
    /**
     * onBackPressed override - make sure the activity gets saved before closing.
     */
    @Override
    public void onBackPressed(){
        if (saveTerm()){
            commonFunctions.makeToast("Successfully saved changes. ");
        }
        else
        {
            commonFunctions.makeToast("Unable to save changes. ");
        }

        finish();
    }

}
