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
import com.wgu.mbuel.wgumobileapp.database.List.MentorList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.CourseDetailsMentorListAdapter;
import com.wgu.mbuel.wgumobileapp.database.adapter.MentorListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.MentorAssignments;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorAssignmentsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorRepo;

import java.util.ArrayList;

public class CourseSelection
        extends AppCompatActivity
        implements ListView.OnItemClickListener,ListView.OnItemLongClickListener{

    public static final int REQUEST_CODE= 1006;
    private static final int MENU_ITEM_SAVE=1;
    private static final int MENU_ITEM_DELETE=2;
    private final String CLASS_NAME = this.getClass().getName();

    private Intent setIntent;
    private Bundle getCourseDetail;
    private Course course = new Course();
    private CommonFunctions commonFunctions = new CommonFunctions();

    //DATABASE objects
    private CourseRepo courseRepo = new CourseRepo();
    private MentorRepo mentorRepo = new MentorRepo();
    private MentorAssignmentsRepo mentorAssignmentsRepo = new MentorAssignmentsRepo();

    //LIST OBJECTS
    private CourseList courseList = new CourseList();
    private ArrayList<MentorAssignmentList> mentorLists;
    private ArrayList<MentorList> mentorDetailList;
    private CourseDetailsMentorListAdapter mentorAssignmentAdapter;
    private MentorListAdapter mentorListAdapter;

    //CREATE OBJECT REFERENCES
    private EditText editTextCourseTitle;
    private Spinner spinnerSelectMentorName;
    private ListView mentorAssignmentListView;

    /**
     * onCreate - creates and instantiates this activity
     * @param savedInstanceState bundle of values
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.getCourseDetail = intent.getExtras(); //Populate bundle
        Log.d(CLASS_NAME,"Course ID: " + this.getCourseDetail.getString(Course.COURSE_ID));

        this.mentorDetailList = this.mentorRepo.readMentorNameList();

        if (commonFunctions.tryParseInt(this.getCourseDetail.getString(Course.COURSE_ID)) && commonFunctions.getInteger() > 0) {

            courseList.setCourseId(this.getCourseDetail.getString(Course.COURSE_ID));
            course.setCourseId(courseList.getCourseId());
            courseList.setCourseTitle(this.getCourseDetail.getString(Course.COURSE_TITLE));
            course.setCourseTitle(courseList.getCourseTitle());
            setTitle(course.getCourseTitle() + " VIEW");
            this.mentorLists = this.mentorAssignmentsRepo.readCourseDetailsMentorAssignments(courseList.getCourseId());
            //loads mentor assignments to this course if it's a course being edited.
            if(!(this.mentorLists.isEmpty()))
            {
                refreshListView();
            }

        }
        else
        {
            setTitle("New Course");
        }
        Log.d(CLASS_NAME,"Converted Course Id: " + commonFunctions.getInteger());
        Log.d(CLASS_NAME, "MentorList is empty?" + this.mentorDetailList.isEmpty());

        MentorList emptyMentor = new MentorList();
        emptyMentor.setMentorName("NONE SELECTED");
        mentorDetailList.add(0,emptyMentor);
        displaySpinner();

        this.editTextCourseTitle = (EditText) findViewById(R.id.editTextCourseTitle);
        editTextCourseTitle.setText(courseList.getCourseTitle());

        Log.d(CLASS_NAME,"Bundle Values: " + this.getCourseDetail.getString(Course.COURSE_TITLE));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * refreshListView - refreshes ListView that is displayed on the main Activity
     */
    private void refreshListView()
    {

        this.mentorLists = this.mentorAssignmentsRepo.readCourseDetailsMentorAssignments(courseList.getCourseId());
        this.mentorAssignmentAdapter = new CourseDetailsMentorListAdapter(App.getContext(),0,this.mentorLists);
        this.mentorAssignmentAdapter.notifyDataSetChanged();

        this.mentorAssignmentListView = (ListView) findViewById(R.id.listMentorAssignments);
        this.mentorAssignmentListView.setAdapter(this.mentorAssignmentAdapter);
        this.mentorAssignmentListView.setOnItemClickListener(this);
        this.mentorAssignmentListView.setOnItemLongClickListener(this);
    }

    /**
     * displaySpinner - displays and deals with click events for Spinner
     */
    private void displaySpinner()
    {
        this.spinnerSelectMentorName = (Spinner) findViewById(R.id.spinnerListMentors);
        //this.spinnerSelectMentorName.setPopupBackgroundResource(R.color.colorPrimary);

        this.mentorListAdapter = new MentorListAdapter(this,android.R.layout.simple_spinner_item,this.mentorDetailList);
        this.mentorListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerSelectMentorName.setAdapter(this.mentorListAdapter);
        this.spinnerSelectMentorName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTitle("Course Creation");
                saveCourseDetails();
                MentorList selectedMentor = (MentorList) parent.getItemAtPosition(position);
                CourseList courseToAssign = readCourseList();
                Mentor convertedMentor = new Mentor();
                convertedMentor.setMentorId(selectedMentor.getMentorId());
                Log.d(CLASS_NAME,"SELECTED MENTOR ID: " + selectedMentor.getMentorId());
                Log.d(CLASS_NAME,"Course to Assign: " + courseToAssign.getCourseTitle());
                //Makes sure the course selection is valid.
                if ((commonFunctions.tryParseInt(courseToAssign.getCourseId()) && commonFunctions.getInteger() >0 ) &&
                        (commonFunctions.tryParseInt(selectedMentor.getMentorId()) && commonFunctions.getInteger() > 0)) {

                    Log.d(CLASS_NAME, "ASSIGN MENTOR: " + selectedMentor.getMentorName());
                    //Makes sure the Mentor you are adding these values to exists in the Mentor table.

                    if (!(mentorAssignmentsRepo.readMentorAssignmentExists(convertedMentor,courseToAssign))) {
                        mentorAssignmentsRepo.create(updateMentorAssignments(selectedMentor, courseToAssign));
                        spinnerSelectMentorName.setSelection(0);
                        //selectionLock = true;
                        refreshListView();
                        //selectionLock = false;
                        commonFunctions.makeToast("Mentor assignment updated. ");

                    }
                    else
                    {
                        commonFunctions.makeToast("Unable to delete add Mentor that already is assigned. ");
                    }
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * updateMentorAssignments - creates copy of MentorAssignments object based on passed bundle
     * @param selectedMentor MentorList passed in
     * @param selectedCourseList CourseList passed in
     * @return MentorAssignments object
     */
    private MentorAssignments updateMentorAssignments(MentorList selectedMentor, CourseList selectedCourseList)
    {
        MentorAssignments updatedMentorAssignmentList = new MentorAssignments();
        updatedMentorAssignmentList.setMentorAssignmentsCourseId(selectedCourseList.getCourseId());
        updatedMentorAssignmentList.setMentorAssignmentsMentorId(selectedMentor.getMentorId());

        return updatedMentorAssignmentList;
    }

    /**
     * readCourseList - returns a copy of this.courseList
     * @return copy of this.courseList
     */
    private CourseList readCourseList()
    {
        return this.courseList;
    }

    /**
     * readCourse - returns copy of this.course
     * @return copy of this.course
     */
    private Course readCourse()
    {
        return this.course;
    }
    /** onCreateOptionsMenu - creates menu via code, filling requirement _____
     *
     * @param menu
     * @return always returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SAVE, Menu.NONE, "save");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "delete");
        return true;
    }
    private void updateCourseDetails()
    {
        this.course.setCourseTitle(editTextCourseTitle.getText().toString());
    }

    /**
     * saveCourseDetails - saves or creates this course.
     * @return
     */
    private int saveCourseDetails()
    {
        updateCourseDetails(); //update course objects from Activity
        int courseId = commonFunctions.tryParseInt(course.getCourseId())?commonFunctions.getInteger():0;
        int saveResults = 0;
        if (!(this.editTextCourseTitle.getText().toString().isEmpty())){
            if (courseId > 0){
                Log.d(CLASS_NAME,"Update existing Course: " + this.course.getCourseTitle());
                saveResults = courseRepo.update(readCourse());
                Log.d(CLASS_NAME,"updated row: " + saveResults);
                commonFunctions.makeToast("Course updated.");

            }
            else
            {
                //Make sure course doesn't already exist in the database.
                if (courseRepo.readCourseExists(readCourse())) {
                    Log.d(CLASS_NAME, "Creating new Course: " + this.editTextCourseTitle.getText().toString());
                    saveResults = courseRepo.create(readCourse());
                    this.course.setCourseId(String.valueOf(saveResults));
                    this.courseList.setCourseId(String.valueOf(saveResults));
                    Log.d(CLASS_NAME, "Created row: " + saveResults);
                    setTitle(this.course.getCourseTitle() + " EDIT");
                    commonFunctions.makeToast("Course created.");
                    displaySpinner();
                }
                else
                {
                    Log.d(CLASS_NAME,"Unable to add a course that already exists.");
                    commonFunctions.makeToast("Cannot add course that is already in table.");
                    //course already exists with this title.
                }

            }


        }
        return saveResults;
    }
    /**
     * onOptionsItemSelected - select Save/Delete and process data
     * @param item
     * @return true/false if action was successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_SAVE:


                return saveCourseDetails() > 0;
            case MENU_ITEM_DELETE:
                updateCourseDetails();
                refreshListView();
                displaySpinner();
                Log.d(CLASS_NAME,"coursesAssigned is empty? " + this.mentorLists.isEmpty());
                int deletedRowId = 0;
                if (mentorLists.isEmpty()){
                    deletedRowId = courseRepo.delete(readCourseList());
                    commonFunctions.makeToast("Course Deleted.");

                }
                else
                {
                    commonFunctions.makeToast("Unable to delete Course with Mentors assigned. ");
                }
                if (deletedRowId > 0)
                {
                    finish();
                    return true;
                }
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }

    }

    /**
     * onBackPressed override - make sure the activity gets saved before closing.
     */
    @Override
    public void onBackPressed(){
        if (saveCourseDetails() > 0){
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
        MentorAssignmentList selectedMentor = (MentorAssignmentList) this.mentorAssignmentListView.getItemAtPosition(position);

        this.setIntent = new Intent(CourseSelection.this,MentorSelection.class);
        MentorList mentorSelected = convertSelectedMentor(selectedMentor);
        Log.d(CLASS_NAME,"selectedMentor.id: " + (selectedMentor.getMentorAssignmentsMentorId()));

        Log.d(CLASS_NAME,"MentorList is null? " + (mentorSelected == null));
        if (mentorSelected != null) {
            setIntentLaunchActivity(mentorSelected);
        }
    }

    /**
     * convertSelectedMentor - returns MentorList object for MentorAssignmentList object passed in
     * @param selectedMentor mentor to convert
     * @return MentorList object
     */
    public MentorList convertSelectedMentor(MentorAssignmentList selectedMentor)
    {
        MentorList convertedMentor = mentorRepo.readSelectedRow(selectedMentor);

        return convertedMentor;
    }

    /**
     * setIntentLaunchActivity - sets up extra data to bundle in to intent, then launches activity
     * @param mentorList mentor list object to bundle
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

        startActivityForResult(this.setIntent,this.REQUEST_CODE);
    }

    /**
     * on long click, delete mentor from assigned mentor list
     * @param parent this Activity
     * @param view - the ListView
     * @param position -position selected
     * @param id - id of position
     * @return true / false for row deleted
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int deletedRowId = 0;
        Log.d(CLASS_NAME,"Delete Mentor assignment.");
        MentorAssignmentList mentorAssignmentToRemove = (MentorAssignmentList) this.mentorAssignmentListView.getItemAtPosition(position);
        deletedRowId = mentorAssignmentsRepo.delete(mentorAssignmentToRemove);
        refreshListView();
        commonFunctions.makeToast("Mentor assignment removed. ");
        Log.d(CLASS_NAME,"Mentor assignment deleted: " + deletedRowId);
        return deletedRowId > 0;
    }
}
