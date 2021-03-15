package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.wgu.mbuel.wgumobileapp.app.SoftKeyboardLsnedRelativeLayout;
import com.wgu.mbuel.wgumobileapp.database.List.AssessmentDetailList;
import com.wgu.mbuel.wgumobileapp.database.List.CourseList;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.CourseListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.CourseDetails;
import com.wgu.mbuel.wgumobileapp.database.repo.AssessmentDetailRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseDetailsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseRepo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TermCourseDetail
        extends AppCompatActivity
        implements View.OnClickListener {
    private static final int MENU_ITEM_SAVE=1;
    private static final int MENU_ITEM_DELETE=2;
    public static final int REQUEST_CODE= 1008;
    private static final String DETAIL_PREFS_NAME = "termCourseDetails";
    private final String CLASS_NAME = this.getClass().getName();

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Date currentDate =  new Date();

    private String currentDay = this.sdf.format(this.currentDate);

    //data objects
    private Intent setIntent;
    private Bundle getTermCourseDetail;
    CommonFunctions commonFunctions = new CommonFunctions();

    //List object
    private TermDetailList selectedCourseDetails;
    //private Term assignedTerm = new Term();
    //private Course select

    //Database objects
    private CourseDetailsRepo courseDetailsRepo = new CourseDetailsRepo();
    private CourseRepo courseRepo = new CourseRepo();


    //Array of TermList objects
    private ArrayList<TermDetailList> termDetailLists;
    private ArrayList<CourseList> listCourses;
    private CourseListAdapter courseListAdapter;

    //List Adapter
    private int termCourseDetailId = 0;

    //Activity objects
    Spinner spinnerSelectCourse;
    EditText editTextStartDate, editTextEndDate, editTextNotes, editTextStatus;
    CheckBox cbStartAlarm, cbEndAlarm;
    Button btnMentorList, btnAssessmnetList;
    ImageButton btnShareNote;

    /**
     * onCreate - loads this activity
     * @param savedInstanceState - bundle of values from previous activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initActivity();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        this.getTermCourseDetail = intent.getExtras();

        this.selectedCourseDetails = new TermDetailList();
        //unbundle data!
        Log.d(CLASS_NAME,"getTermCourseDetail is null? " + (this.getTermCourseDetail == null));
        if (this.getTermCourseDetail != null)
        {

            this.selectedCourseDetails.setCourseDetailsTermId(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_TERM_ID));
            this.selectedCourseDetails.setCourseDetailsCourseId(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_COURSE_ID));
            this.selectedCourseDetails.setCourseDetailsId(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_ID));

        }
        else
        {
            restorePrefs();
        }
        initObjects();
        displaySpinner();

        setActivityValues();

        SoftKeyboardLsnedRelativeLayout layout = (SoftKeyboardLsnedRelativeLayout) findViewById(R.id.activityTermCourseDetail);
        Log.d(CLASS_NAME,"Layout is null? " + (layout == null));
        Log.d(CLASS_NAME,"R.id.activityTermCourseDetail is null? " + (R.id.activityTermCourseDetail == 0));
        layout.addSoftKeyboardLsner(new SoftKeyboardLsnedRelativeLayout.SoftKeyboardLsner() {
            @Override
            public void onSoftKeyboardShow() {
                Log.d(CLASS_NAME,"Soft Keyboard shown");
                btnMentorList.setVisibility(View.GONE);
                btnAssessmnetList.setVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardHide() {
                Log.d(CLASS_NAME,"Soft Keyboard hidden.");
                btnMentorList.setVisibility(View.VISIBLE);
                btnAssessmnetList.setVisibility(View.VISIBLE);
            }
        });

    }
    //1

    /**
     * initObjects - initialize the object that holds the details for the Course assigned to the selected Term.
     */
    private void initObjects()
    {
        if (this.getTermCourseDetail != null) {
            this.selectedCourseDetails.setTermTitle(this.getTermCourseDetail.getString(CourseDetails.TERM_TITLE));
            this.selectedCourseDetails.setCourseTitle(this.getTermCourseDetail.getString(CourseDetails.COURSE_TITLE));
            this.termCourseDetailId = commonFunctions.tryParseInt(this.selectedCourseDetails.getCourseDetailsId())?commonFunctions.getInteger():0;

            if (termCourseDetailId > 0) {
                setTitle(this.selectedCourseDetails.getTermTitle() + " | " + this.selectedCourseDetails.getCourseTitle() + " TRACKER");

                this.selectedCourseDetails.setCourseDetailsStatus(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_STATUS));
                this.selectedCourseDetails.setCourseDetailsStartDate(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_START_DATE));
                this.selectedCourseDetails.setCourseDetailsEndDate(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_END_DATE));
                this.selectedCourseDetails.setCourseDetailsStartAlarm(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_START_ALARM));
                this.selectedCourseDetails.setCourseDetailsEndAlarm(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_END_ALARM));
                this.selectedCourseDetails.setCourseDetailsNotes(this.getTermCourseDetail.getString(CourseDetails.COURSE_DETAILS_NOTES));
            }
            else
            {
                Log.d(CLASS_NAME,"Creating new TermDetailList");
                setTitle(this.selectedCourseDetails.getTermTitle() + " | " + "NEW COURSE TRACKER");
            }
        }
        else
        {
            Log.d(CLASS_NAME,"Creating new TermDetailList");
        }

    }
    //3

    /**
     * initActivity - initializes the activity with references pointing to XML declaration.
     */
    private void initActivity()
    {
        this.editTextStartDate = (EditText) findViewById(R.id.editTextCourseStartDate);
        this.editTextEndDate = (EditText) findViewById(R.id.editTextCourseEndDate);
        this.editTextStatus = (EditText) findViewById(R.id.editTextCourseDetailStatus);
        this.editTextNotes = (EditText) findViewById(R.id.editTextTermCourseDetailNotes);
        this.cbStartAlarm = (CheckBox) findViewById(R.id.checkBoxCourseStartAlarm);
        this.cbEndAlarm = (CheckBox) findViewById(R.id.checkBoxCourseEndAlarm);
        this.btnAssessmnetList = (Button) findViewById(R.id.btnAssessmentList);
        this.btnAssessmnetList.setOnClickListener(this);
        this.btnMentorList = (Button) findViewById(R.id.btnAssignedMentors);
        this.btnMentorList.setOnClickListener(this);
        this.btnShareNote = (ImageButton) findViewById(R.id.btnTermCourseDetailNotesShare);
        this.btnShareNote.setOnClickListener(this);
    }

    /**
     * setActivityValues() - sets activity values
     */
    private void setActivityValues()
    {
        if (this.selectedCourseDetails.getCourseDetailsId().equals("0"))
        {
            this.editTextStartDate.setText(this.currentDay);
        }
        else {
            this.editTextStartDate.setText(this.selectedCourseDetails.getCourseDetailsStartDate());
        }

            //Log.d(CLASS_NAME,"Start Date: " + this.selectedCourseDetails.getCourseDetailsStartDate());
        this.editTextEndDate.setText(this.selectedCourseDetails.getCourseDetailsEndDate());
        this.editTextNotes.setText(this.selectedCourseDetails.getCourseDetailsNotes());
        this.editTextStatus.setText(this.selectedCourseDetails.getCourseDetailsStatus());

        if (commonFunctions.tryParseInt(this.selectedCourseDetails.getCourseDetailsStartAlarm())) {
            this.cbStartAlarm.setChecked(commonFunctions.getInteger() > 0);
            if (this.cbStartAlarm.isChecked())
            {
                displayAlert(this.selectedCourseDetails.getCourseDetailsStartDate(),"START ALARM");
            }
        }
        if (commonFunctions.tryParseInt(this.selectedCourseDetails.getCourseDetailsEndAlarm())){
            this.cbEndAlarm.setChecked(commonFunctions.getInteger() > 0);
            if (this.cbEndAlarm.isChecked())
            {
                displayAlert(this.selectedCourseDetails.getCourseDetailsEndDate(),"END ALARM");
            }
        }

        if (commonFunctions.tryParseInt(this.selectedCourseDetails.getCourseDetailsId())  && commonFunctions.getInteger()  > 0){
            Log.d(CLASS_NAME,"ATTEMPT TO SET SPINNER.");
            //selectedCourseList = courseRepo.readSelectedCourse(this.selectedCourseDetails);
            for (int i = 0; i < this.courseRepo.readCourseRowCount();i++)
            {
                CourseList selectedCourse = (CourseList) this.spinnerSelectCourse.getItemAtPosition(i+1);
                if (selectedCourse.getCourseTitle().equals(this.selectedCourseDetails.getCourseTitle())){
                    this.spinnerSelectCourse.setSelection(i+1);
                    break;
                }
            }
            //this.spinnerSelectCourse.setSelection(this.courseListAdapter.getPosition(this.selectedCourseList));
        }
    }


    //2

    /**
     * displaySpinner - displaySpinner and set select
     */
    private void displaySpinner()
    {
        this.listCourses = courseRepo.readCourseList();
        if (this.listCourses.isEmpty())
        {
            commonFunctions.makeToast("Cannot continue, no courses to assign.");
            finish();
        }
        CourseList emptyCourseList = new CourseList();
        emptyCourseList.setCourseTitle("NONE SELECTED");
        this.listCourses.add(0,emptyCourseList);
        this.courseListAdapter = new CourseListAdapter(this, android.R.layout.simple_spinner_item, this.listCourses);
        this.courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerSelectCourse = (Spinner) findViewById(R.id.spinnerTermCourseDetailTitle);

        this.spinnerSelectCourse.setAdapter(courseListAdapter);

        this.spinnerSelectCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CourseList selectedCourse = (CourseList) parent.getItemAtPosition(position);
                selectedCourseDetails.setCourseDetailsCourseId(selectedCourse.getCourseId());
                selectedCourseDetails.setCourseTitle(selectedCourse.getCourseTitle());

            } // to close the onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    /**
     * getActivityValues - populates class selectedCourseDetails with values from Activity and returns a copy of the object
     * @return TermDetailList from activity
     */
    private TermDetailList getActivityValues()
    {
        this.selectedCourseDetails.setCourseDetailsStartDate(this.editTextStartDate.getText().toString());
        this.selectedCourseDetails.setCourseDetailsEndDate(this.editTextEndDate.getText().toString());
        this.selectedCourseDetails.setCourseDetailsNotes(this.editTextNotes.getText().toString());
        this.selectedCourseDetails.setCourseDetailsStatus(this.editTextStatus.getText().toString());
        this.selectedCourseDetails.setCourseDetailsStartAlarm(this.cbStartAlarm.isChecked()?"1":"0");

        //DONE:ALERT

        this.selectedCourseDetails.setCourseDetailsEndAlarm(this.cbEndAlarm.isChecked()?"1":"0");
        return this.selectedCourseDetails;
    }

    /**
     * displayAlert() displays alert for due dates.
     * @param date start or end date to compare
     * @param callback text that will be displayed in Alert.
     */
    private void displayAlert(String date, String callback)
    {
        Log.d(CLASS_NAME,"Current Day String: " + this.currentDay);
        if (this.currentDay.equals(date)) {
            new AlertDialog.Builder(TermCourseDetail.this)
                .setMessage(callback + " day is here!")
                .setCancelable(true).create().show();
        }
    }

    /**
     * deleteCourseAssignment - delete the passed TermDetail Assignment
     * @param termToDelete - TermDetailList object to delete
     * @return true if able to delete, false if it can't
     */
    private boolean deleteCourseAssignment(TermDetailList termToDelete)
    {
        return courseDetailsRepo.delete(termToDelete) > 0;

    }

    /**
     * saveCourse saves activity values to table
     * @param termToSave term to save to database
     * @return true or false for success
     */
    private boolean saveCourse(TermDetailList termToSave)
    {
        Log.d(CLASS_NAME,"TERM ID CHECK 4 - Term Id in saveCourse: " + termToSave.getCourseDetailsTermId());
        Log.d(CLASS_NAME,"Start date in sC: " + termToSave.getCourseDetailsStartDate());
        Log.d(CLASS_NAME,"End date in sC: " + termToSave.getCourseDetailsEndDate());

        //DONE: implement code to determine if this is a new or edited object, then create or update the CourseDetailsRepo
        int saveResults = 0;
        int courseDetailsId = (commonFunctions.tryParseInt(termToSave.getCourseDetailsId())?commonFunctions.getInteger():0);

        if (!termToSave.getCourseDetailsCourseId().equals("0") && !termToSave.getCourseDetailsStartDate().isEmpty() && !termToSave.getCourseDetailsEndDate().isEmpty()){
            if (courseDetailsId > 0){
                Log.d(CLASS_NAME,"Update existing Course Assignment: " + termToSave.getCourseTitle());
                Log.d(CLASS_NAME,"Notes before saving: " + termToSave.getCourseDetailsNotes());
                saveResults = courseDetailsRepo.update(termToSave);
                Log.d(CLASS_NAME,"updated row: " + saveResults);
                commonFunctions.makeToast("Successfully saved changes. ");
                return saveResults > 0;

            }
            else
            {
                Log.d(CLASS_NAME,"Creating new Course Assignment: " + termToSave.getCourseTitle());
                saveResults = courseDetailsRepo.create(termToSave);

                Log.d(CLASS_NAME,"Created row: " + saveResults);
                //displaySpinner();
                if (saveResults > 0)
                {
                    Log.d(CLASS_NAME,"Created new assignment.");
                    commonFunctions.makeToast("Successfully created new course assignment. ");
                    setTitle(this.selectedCourseDetails.getTermTitle() + " | " + this.selectedCourseDetails.getCourseTitle() + " TRACKER");
                    this.selectedCourseDetails.setCourseDetailsId(String.valueOf(saveResults));
                    return true;
                }
                else
                {
                    Log.d(CLASS_NAME,"problem creating new assignment.");
                    return false;
                }
            }
        }
        else
        {
            if (this.spinnerSelectCourse.getSelectedItem().toString().equals("NONE SELECTED")) {
                commonFunctions.makeToast("Cannot save without a course selection.");
            }
            else
            {
                commonFunctions.makeToast("Cannot save without start and end date.");
            }
            return false;
        }


    }
    /**
     * onCreateOptionsMenu - creates menu bar in top right of layout
     * @param menu menu to display
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SAVE, Menu.NONE, "save");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "delete");
        return true;
    }

    /**
     * onOptionsItemSelected - deal with click events from action bar
     * @param item in menu
     * @return true false for menu action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SAVE:

                return saveCourse(getActivityValues());


            case MENU_ITEM_DELETE:
                if (deleteCourseAssignment(deleteAssessments(getActivityValues()))) {
                    Log.d(CLASS_NAME, "Assessments removed. ");
                    finish();
                }
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    /**
     * deleteAssessments, deletes assessments attached to course
     * @param courseAssignmentToDelete assessments to delete based on course details ID
     * @return TermDetailList object that is passed on to the next stage
     */
    private TermDetailList deleteAssessments(TermDetailList courseAssignmentToDelete) {
        AssessmentDetailRepo assessmentRepo = new AssessmentDetailRepo();
        AssessmentDetailList assessmentDetail = new AssessmentDetailList();
        assessmentDetail.setAssessmentDetailCourseDetailId(courseAssignmentToDelete.getCourseDetailsId());

        if(assessmentRepo.readRowExists(assessmentDetail))
        {
            assessmentRepo.deleteCourse(assessmentDetail);
        }
        //DONE: return TermDetailList object to use in next stage
        return courseAssignmentToDelete;
    }

    /**
     * onClick override - determines which button was pressed then takes appropriate action to launch next activity.
     * @param v - view (Button pressed)
     */
    @Override
    public void onClick(View v) {
        if (saveCourse(getActivityValues())) {
            if (v == findViewById(R.id.btnAssessmentList)) {
                //DONE: save Activity before switching to next screen
                this.setIntent = new Intent(TermCourseDetail.this, AssessmentView.class);

                this.setIntent.putExtra(CourseDetails.COURSE_DETAILS_ID,this.selectedCourseDetails.getCourseDetailsId());
                this.setIntent.putExtra(CourseDetails.TERM_TITLE,this.selectedCourseDetails.getTermTitle());
                Log.d(CLASS_NAME,"Passing this term title: " + this.selectedCourseDetails.getTermTitle());
                this.setIntent.putExtra(CourseDetails.COURSE_TITLE,this.selectedCourseDetails.getCourseTitle());
                setIntentLaunchActivity();
            }
            if (v == findViewById(R.id.btnTermCourseDetailNotesShare)) {
                //DONE: open share window, intent loaded with notes
                this.setIntent = new Intent(Intent.ACTION_SEND);
                this.setIntent.setType("text/plain");
                //this.setIntent.putExtra(Intent.EXTRA_SUBJECT,"Notes from course: " + this.selectedCourseDetails.getCourseTitle());
                this.setIntent.putExtra(Intent.EXTRA_TEXT,"Notes from Course " + this.selectedCourseDetails.getCourseTitle() + "\n" +
                        "NOTES: " + this.editTextNotes.getText().toString());
                startActivity(Intent.createChooser(this.setIntent,"Notes from Course"));

            }
            if (v == findViewById(R.id.btnAssignedMentors)) {
                this.setIntent = new Intent(TermCourseDetail.this,CourseSelection.class);
                this.setIntent.putExtra(Course.COURSE_ID,this.selectedCourseDetails.getCourseDetailsCourseId());
                Log.d(CLASS_NAME,"Course ID: " + this.selectedCourseDetails.getCourseDetailsCourseId());
                this.setIntent.putExtra(Course.COURSE_TITLE,this.selectedCourseDetails.getCourseTitle());
                setIntentLaunchActivity();
            }
        }
        else {
        //DONE: toast message with error description
            commonFunctions.makeToast("Unable to save course details.");
        }

    }

    /**
     * setIntentLaunchActivity
     * intent object is packed with values before calling this method
     * initiates call to next Activity that is setup in the OnClick method above.
     */
    private void setIntentLaunchActivity()
    {
        getActivityValues();
        writePrefs();

        this.selectedCourseDetails = null;
        this.getTermCourseDetail = null;

        startActivityForResult(this.setIntent,REQUEST_CODE);
    }
    //DONE: need to close form after delete. - NOT CURRENTLY WORKING
    @Override
    public void onBackPressed(){
        if (saveCourse(getActivityValues())){
            commonFunctions.makeToast("Successfully saved changes. ");
        }
        else
        {
            commonFunctions.makeToast("Unable to save changes. ");
        }
        SharedPreferences.Editor setTcdValues = getSharedPreferences(DETAIL_PREFS_NAME, MODE_PRIVATE).edit();
        setTcdValues.clear();
        setTcdValues.apply();
        this.selectedCourseDetails = null;
        this.getTermCourseDetail = null;

        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AssessmentView.REQUEST_CODE && resultCode == RESULT_OK)
        {
            Log.d(CLASS_NAME,"Results: " + data.getCategories());
        }

        if (this.selectedCourseDetails == null)
        {
            this.selectedCourseDetails = new TermDetailList();
        }

        restorePrefs();

    }
    private void writePrefs()
    {
        SharedPreferences.Editor setTcdValues = getSharedPreferences(DETAIL_PREFS_NAME, MODE_PRIVATE).edit();

        setTcdValues.putString(CourseDetails.COURSE_DETAILS_ID,this.selectedCourseDetails.getCourseDetailsId());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_COURSE_ID,this.selectedCourseDetails.getCourseDetailsCourseId());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_TERM_ID,this.selectedCourseDetails.getCourseDetailsTermId());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_START_DATE,this.selectedCourseDetails.getCourseDetailsStartDate());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_END_DATE,this.selectedCourseDetails.getCourseDetailsEndDate());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_START_ALARM, this.selectedCourseDetails.getCourseDetailsStartAlarm());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_END_ALARM,this.selectedCourseDetails.getCourseDetailsEndAlarm());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_STATUS,this.selectedCourseDetails.getCourseDetailsStatus());
        Log.d(CLASS_NAME,"Status written to prefs - " + this.selectedCourseDetails.getCourseDetailsStatus());
        setTcdValues.putString(CourseDetails.COURSE_DETAILS_NOTES,this.selectedCourseDetails.getCourseDetailsNotes());
        Log.d(CLASS_NAME,"Notes written to prefs - " + this.selectedCourseDetails.getCourseDetailsNotes());
        setTcdValues.putString(CourseDetails.COURSE_TITLE,this.selectedCourseDetails.getCourseTitle());
        setTcdValues.putString(CourseDetails.TERM_TITLE,this.selectedCourseDetails.getTermTitle());
        setTcdValues.putString("ACTIVITY_TITLE",this.getTitle().toString());

        setTcdValues.apply();
    }

    private void restorePrefs() {
        SharedPreferences setTcdValues = getSharedPreferences(DETAIL_PREFS_NAME, MODE_PRIVATE);

        this.selectedCourseDetails.setCourseDetailsId(setTcdValues.getString(CourseDetails.COURSE_DETAILS_ID,this.selectedCourseDetails.getCourseDetailsId()));
        this.selectedCourseDetails.setCourseDetailsCourseId(setTcdValues.getString(CourseDetails.COURSE_DETAILS_COURSE_ID,this.selectedCourseDetails.getCourseDetailsCourseId()));
        this.selectedCourseDetails.setCourseDetailsTermId(setTcdValues.getString(CourseDetails.COURSE_DETAILS_TERM_ID,this.selectedCourseDetails.getCourseDetailsTermId()));
        this.selectedCourseDetails.setCourseDetailsStartDate(setTcdValues.getString(CourseDetails.COURSE_DETAILS_START_DATE,this.selectedCourseDetails.getCourseDetailsStartDate()));
        this.selectedCourseDetails.setCourseDetailsEndDate(setTcdValues.getString(CourseDetails.COURSE_DETAILS_END_DATE,this.selectedCourseDetails.getCourseDetailsEndDate()));
        this.selectedCourseDetails.setCourseDetailsStartAlarm(setTcdValues.getString(CourseDetails.COURSE_DETAILS_START_ALARM, this.selectedCourseDetails.getCourseDetailsStartAlarm()));
        this.selectedCourseDetails.setCourseDetailsEndAlarm(setTcdValues.getString(CourseDetails.COURSE_DETAILS_END_ALARM,this.selectedCourseDetails.getCourseDetailsEndAlarm()));
        this.selectedCourseDetails.setCourseDetailsStatus(setTcdValues.getString(CourseDetails.COURSE_DETAILS_STATUS,this.selectedCourseDetails.getCourseDetailsStatus()));
        this.selectedCourseDetails.setCourseDetailsNotes(setTcdValues.getString(CourseDetails.COURSE_DETAILS_NOTES,this.selectedCourseDetails.getCourseDetailsNotes()));
        this.selectedCourseDetails.setCourseTitle(setTcdValues.getString(CourseDetails.COURSE_TITLE,this.selectedCourseDetails.getCourseTitle()));
        this.selectedCourseDetails.setTermTitle(setTcdValues.getString(CourseDetails.TERM_TITLE,this.selectedCourseDetails.getTermTitle()));
        this.setTitle(setTcdValues.getString("ACTIVITY_TITLE",this.selectedCourseDetails.getCourseTitle()));
        initActivity();
        displaySpinner();
        setActivityValues();

    }

}
