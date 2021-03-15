package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.CourseList;
import com.wgu.mbuel.wgumobileapp.database.List.MentorAssignmentList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.CourseListAdapter;
import com.wgu.mbuel.wgumobileapp.database.adapter.MentorDetailsCourseListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.MentorAssignments;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorAssignmentsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorRepo;

import java.util.ArrayList;

public class MentorSelection
        extends AppCompatActivity
        implements ListView.OnItemClickListener, ListView.OnItemLongClickListener{
    public static final int REQUEST_CODE= 1007;
    private final String CLASS_NAME = this.getClass().getName();
    private static final int MENU_ITEM_SAVE=1;
    private static final int MENU_ITEM_DELETE=2;

    //Data objects
    private Mentor mentor = new Mentor();
    private Bundle getMentorDetails;
    private Intent setIntent;
    private CommonFunctions commonFunctions = new CommonFunctions();

    //Database
    private MentorRepo mentorRepo = new MentorRepo();
    private CourseRepo courseRepo = new CourseRepo();
    private MentorAssignmentsRepo mentorAssignmentsRepo = new MentorAssignmentsRepo();

    //Lists
    private ArrayList<MentorAssignmentList> mentorDetailAssignedCourses;
    private ArrayList<CourseList> listCourses;
    private MentorDetailsCourseListAdapter mentorDetailsCourseListAdapter;
    private CourseListAdapter courseListAdapter;

    //Objects
    private EditText mentorName, workEmail, homeEmail, cellPhone, homePhone;
    private Spinner spinnerSelectCourse;
    private ListView listViewAssignedCourses;

    /**
     * onCreate() main method that instantiates mentor detail view
     * @param savedInstanceState passed bundle value
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.getMentorDetails = intent.getExtras();
        this.mentor.setMentorId(this.getMentorDetails.getString(Mentor.MENTOR_ID));
        this.listCourses = this.courseRepo.readCourseList();
        //populate mentor object if this is an edit


        createMentorDetails();
        refreshAssignmentList();

        CourseList emptyValue = new CourseList();
        emptyValue.setCourseId("0");
        emptyValue.setCourseTitle("NONE");
        this.listCourses.add(0,emptyValue);

        displaySpinner();
        //Populate view with either default values (empty) or loaded values
        this.mentorName = (EditText) findViewById(R.id.editTextMentorName);
        this.mentorName.setText(this.mentor.getMentorName());

        this.workEmail = (EditText) findViewById(R.id.editTextMentorWorkEmail);
        this.workEmail.setText(this.mentor.getMentorWorkEmail());

        this.homeEmail = (EditText) findViewById(R.id.editTextMentorHomeEmail);
        this.homeEmail.setText(this.mentor.getMentorHomeEmail());

        this.cellPhone = (EditText) findViewById(R.id.editTextMentorCellPhone);
        this.cellPhone.setText(this.mentor.getMentorCellPhoneNumber());

        this.homePhone = (EditText) findViewById(R.id.editTextMentorHomePhone);
        this.homePhone.setText(this.mentor.getMentorHomePhoneNumber());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * createMentorDetails - populate private Mentor object with values from passed bundle
     */
    private void createMentorDetails()
    {
        this.mentor.setMentorName(this.getMentorDetails.getString(Mentor.MENTOR_NAME));
        this.mentor.setMentorHomePhoneNumber(this.getMentorDetails.getString(Mentor.MENTOR_HOME_PHONE_NUMBER));
        this.mentor.setMentorCellPhoneNumber(this.getMentorDetails.getString(Mentor.MENTOR_CELL_PHONE_NUMBER));
        this.mentor.setMentorWorkEmail(this.getMentorDetails.getString(Mentor.MENTOR_WORK_EMAIL));
        this.mentor.setMentorHomeEmail(this.getMentorDetails.getString(Mentor.MENTOR_HOME_EMAIL));
        this.mentor.setMentorCreated(this.getMentorDetails.getString(Mentor.MENTOR_CREATED));
        if (this.mentor.getMentorId().equals("0"))
        {
            setTitle("Mentor Creation");
        }
        else
        {
            setTitle(this.mentor.getMentorName() + " EDIT");
        }

    }

    /**
     * refreshAssignmentList -
     * on save and load refreshes saved assignment view
     */
    private void refreshAssignmentList()
    {
        this.mentorDetailAssignedCourses = this.mentorAssignmentsRepo.readMentorDetailsCourseAssignments(this.mentor.getMentorId());
        this.mentorDetailsCourseListAdapter = new MentorDetailsCourseListAdapter(App.getContext(),0,mentorDetailAssignedCourses);
        this.mentorDetailsCourseListAdapter.notifyDataSetChanged();

        this.listViewAssignedCourses = (ListView) findViewById(R.id.listCourseAssignments);
        this.listViewAssignedCourses.setAdapter(this.mentorDetailsCourseListAdapter);
        this.listViewAssignedCourses.setOnItemClickListener(this);
        this.listViewAssignedCourses.setOnItemLongClickListener(this);
    }

    /**
     * displaySpinner -
     * code to load and deal with spinner clicks
     */
    private void displaySpinner()
    {

        this.spinnerSelectCourse = (Spinner) findViewById(R.id.spinnerAssignCourse);
        courseListAdapter = new CourseListAdapter(this, android.R.layout.simple_spinner_item, this.listCourses);


        this.courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectCourse.setAdapter(this.courseListAdapter);

        spinnerSelectCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                saveMentorDetails();
                int mentorId = ((commonFunctions.tryParseInt(mentor.getMentorId())?commonFunctions.getInteger():0));
                Log.d(CLASS_NAME,"onItemSelected in Spinner Mentor ID: " + mentorId);

                if (mentorId> 0) {
                    CourseList selectedCourse = (CourseList) parent.getItemAtPosition(position);
                    Log.d(CLASS_NAME, "SELECTED COURSE ID: " + selectedCourse.getCourseId());
                    //Makes sure the course selection is valid.
                    if (commonFunctions.tryParseInt(selectedCourse.getCourseId()) && commonFunctions.getInteger() > 0) {

                        Log.d(CLASS_NAME, "ADD COURSE: " + selectedCourse.getCourseTitle());
                        //Makes sure the Mentor you are adding these values to exists in the Mentor table.
                        Mentor updatedMentorValues = updateMentorDetails();
                        if (!(mentorAssignmentsRepo.readMentorAssignmentExists(updatedMentorValues, selectedCourse))) {
                            mentorAssignmentsRepo.create(updateMentorAssignments(selectedCourse, updatedMentorValues));
                            spinnerSelectCourse.setSelection(0);
                            refreshAssignmentList();
                        }

                    }
                }
                else
                {
                    commonFunctions.makeToast("Unable to set assigned course, missing Mentor information.");
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * returns id values that will be saved to assignment database
     * @param selectedCourse CourseList object from spinner
     * @param mentor mentor from local variable
     * @return MentorAssignment object
     */
    private MentorAssignments updateMentorAssignments(CourseList selectedCourse,Mentor mentor)
    {
        MentorAssignments updatedMentorAssignments = new MentorAssignments();
        updatedMentorAssignments.setMentorAssignmentsCourseId(selectedCourse.getCourseId());
        updatedMentorAssignments.setMentorAssignmentsMentorId(mentor.getMentorId());

        return updatedMentorAssignments;
    }

    /**
     * updateMentorDetails - updates local field object containing the mentor details, then returns a copy.
     * @return copy of local Mentor object
     */
    private Mentor updateMentorDetails()
    {
        Mentor updatedMentorDetails = this.mentor;
        if (commonFunctions.tryParseInt(this.mentor.getMentorId()) && commonFunctions.getInteger() > 0) {
            updatedMentorDetails.setMentorId(this.mentor.getMentorId());
        }
        this.mentor.setMentorName(mentorName.getText().toString());
        updatedMentorDetails.setMentorName(this.mentor.getMentorName());

        this.mentor.setMentorHomePhoneNumber(homePhone.getText().toString());
        updatedMentorDetails.setMentorHomePhoneNumber(this.mentor.getMentorHomePhoneNumber());

        this.mentor.setMentorCellPhoneNumber(cellPhone.getText().toString());
        updatedMentorDetails.setMentorCellPhoneNumber(this.mentor.getMentorCellPhoneNumber());

        this.mentor.setMentorHomeEmail(homeEmail.getText().toString());
        updatedMentorDetails.setMentorHomeEmail(this.mentor.getMentorHomeEmail());

        this.mentor.setMentorWorkEmail(workEmail.getText().toString());
        updatedMentorDetails.setMentorWorkEmail(this.mentor.getMentorWorkEmail());

        return updatedMentorDetails;
    }

    /**
     * saves Mentor Details.
     * @return integer of row added or updated in Database
     */
    private int saveMentorDetails()
    {
        updateMentorDetails();
        int saveResults = 0;
        if (!(this.mentorName.getText().toString().isEmpty())){
            if (commonFunctions.tryParseInt(mentor.getMentorId()) && commonFunctions.getInteger() > 0){
                Log.d(CLASS_NAME,"Update existing mentor: " + this.mentor.getMentorName());
                saveResults = mentorRepo.update(updateMentorDetails());
                Log.d(CLASS_NAME,"updated row: " + saveResults);
                commonFunctions.makeToast("Updated mentor details.");

            }
            else
            {
                Log.d(CLASS_NAME,"Creating new Mentor: " + this.mentorName.getText().toString());
                if (mentorRepo.readMentorNameExists(updateMentorDetails().getMentorName())) {
                    saveResults = mentorRepo.create(updateMentorDetails());
                    this.mentor.setMentorId(String.valueOf(saveResults));
                    refreshAssignmentList();
                    Log.d(CLASS_NAME, "Created row: " + saveResults);
                    commonFunctions.makeToast("Created Mentor Details.");
                    //displaySpinner();
                }
                else
                {
                    Log.d(CLASS_NAME,"CANNOT UPDATE MENTOR ALREADY IN DATABASE.");
                    commonFunctions.makeToast("CANNOT UPDATE MENTOR ALREADY IN DATABASE.");

                }

            }


        }
        return saveResults;
    }

    /** onCreateOptionsMenu - creates menu via code, filling requirement _____
     *
     * @param menu displayed
     * @return always returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SAVE, Menu.NONE, "save");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "delete");
        return true;
    }

    /**
     * onOptionsItemSelected - select Save/Delete and process data
     * @param item selected
     * @return true/false if action was successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SAVE:


                return saveMentorDetails() > 0;
            case MENU_ITEM_DELETE:
                updateMentorDetails();
                Log.d(CLASS_NAME,"coursesAssigned is empty? " + this.mentorDetailAssignedCourses.isEmpty());
                int deletedRowId = 0;
                if (mentorDetailAssignedCourses.isEmpty()) {
                    deletedRowId = mentorRepo.delete(this.mentor);

                    if (deletedRowId > 0) {
                        commonFunctions.makeToast("Mentor deleted from table.");
                        finish();
                        return true;
                    }

                }
                else
                {
                    commonFunctions.makeToast("Unable to delete Mentor with courses assigned. ");
                }
                finish();
                return deletedRowId > 0;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }

    }

    /**
     * onBackPressed
     * Over rides back button press to make sure activity results get saved to the database.
     */
    @Override
    public void onBackPressed(){
        if (saveMentorDetails() > 0){
            commonFunctions.makeToast("Successfully saved changes. ");
        }
        else
        {
            commonFunctions.makeToast("Unable to save changes. ");
        }

        finish();
    }
    /**
     * onItemClick - process ListView item click
     * @param parent this Activity
     * @param view - the ListView
     * @param position -position selected
     * @param id - id of position
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(CLASS_NAME,"ListView CLICKED");
        MentorAssignmentList selectedCourseAssignment = (MentorAssignmentList) this.listViewAssignedCourses.getItemAtPosition(position);
        Log.d(CLASS_NAME,"Selected Assignment: " + selectedCourseAssignment.getCourseTitle());

        this.setIntent = new Intent(MentorSelection.this,CourseSelection.class);

        setIntentLaunchActivity(createCourseList(selectedCourseAssignment));
    }

    /**
     * returns copy of CourseList object
     * @param course course object to convert
     * @return CourseList from MentorAssignmentList
     */
    private CourseList createCourseList(MentorAssignmentList course)
    {
        CourseList selectedCourse = new CourseList();
        selectedCourse.setCourseTitle(course.getCourseTitle());
        selectedCourse.setCourseId(course.getMentorAssignmentsCourseId());

        return selectedCourse;
    }

    /**
     * setIntentLaunchActivity - launches next activity
     * @param courseList courselist to pass to next activity
     */
    private void setIntentLaunchActivity(CourseList courseList)
    {
        setIntent.putExtra(Course.COURSE_ID,courseList.getCourseId());
        setIntent.putExtra(Course.COURSE_TITLE,courseList.getCourseTitle());
        setIntent.putExtra(Course.COURSE_CREATED, courseList.getCourseCreated());


        startActivityForResult(this.setIntent,REQUEST_CODE);
    }

    /**
     * on long click, delete item from assigned course list
     * @param parent this Activity
     * @param view - the ListView
     * @param position -position selected
     * @param id - id of position
     * @return true / false for row deleted
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int deletedRowId = 0;
        Log.d(CLASS_NAME,"Delete course assignment.");
        MentorAssignmentList courseAssignmentToRemove = (MentorAssignmentList) this.listViewAssignedCourses.getItemAtPosition(position);
        deletedRowId = mentorAssignmentsRepo.delete(courseAssignmentToRemove);
        refreshAssignmentList();

        Log.d(CLASS_NAME,"Course Assignment deleted: " + deletedRowId);
        return deletedRowId > 0;
    }


}
