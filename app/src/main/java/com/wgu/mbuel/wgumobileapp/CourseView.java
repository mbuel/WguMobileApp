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
import com.wgu.mbuel.wgumobileapp.database.List.CourseList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.CourseListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorAssignmentsRepo;

import java.util.ArrayList;

public class CourseView
        extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener {
    public static final int REQUEST_CODE= 1003;
    private final String CLASS_NAME = this.getClass().getName();

    private Intent setIntent;
    private Course course;
    private CommonFunctions commonFunctions = new CommonFunctions();

    //Database
    private CourseRepo courseRepo = new CourseRepo();
    private MentorAssignmentsRepo mentorAssignmentsRepo = new MentorAssignmentsRepo();
    private ArrayList<CourseList> courseLists;
    private CourseListAdapter courseListAdapter;

    private ListView courseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabCourseList = (FloatingActionButton) findViewById(R.id.fabCourseList);
        fabCourseList.setOnClickListener(this);

        setTitle("Course List");

        this.courseLists = this.courseRepo.readCourseList();
        if (this.courseLists.size() > 0 )
        {
            Log.d(this.CLASS_NAME,"Size of termLists: " + this.courseLists.size());
            for(CourseList courseListObj : this.courseLists)
            {
                Log.d(this.CLASS_NAME,"Term Title: " + courseListObj.getCourseTitle());
            }

        }

        refreshListView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void refreshListView()
    {
        courseLists = courseRepo.readCourseList();
        courseListAdapter = new CourseListAdapter(App.getContext(),0,courseLists);
        courseListAdapter.notifyDataSetChanged();

        courseListView = (ListView) findViewById(R.id.listCourses);
        courseListView.setAdapter(courseListAdapter);
        courseListView.setOnItemClickListener(this);
        courseListView.setOnItemLongClickListener(this);

    }

    /**
     * insertCourse is used for testing purposes, it creates three courses in the database.
     */
    private void insertCourse()
    {
        this.course = new Course();
        course.setCourseTitle("C181");
        courseRepo.create(course);

        this.course = new Course();
        course.setCourseTitle("C123");
        courseRepo.create(course);

        this.course = new Course();
        course.setCourseTitle("P213");
        courseRepo.create(course);

    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.fabCourseList)){
            Snackbar.make(v, "Creating new course", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            this.setIntent = new Intent(CourseView.this,CourseSelection.class);
            CourseList emptyCourseList = new CourseList();

            setIntentLaunchActivity(emptyCourseList);

        }
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
        CourseList selectedCourse = (CourseList) this.courseListView .getItemAtPosition(position);

        this.setIntent = new Intent(CourseView.this,CourseSelection.class);

        setIntentLaunchActivity(selectedCourse);

    }

    /**
     * setIntentLaunchActivity - takes object and populates intent for next Activity to unpack
     * @param courseList - object being packed into intent bundle
     */
    private void setIntentLaunchActivity(CourseList courseList)
    {
        setIntent.putExtra(Course.COURSE_ID,courseList.getCourseId());
        setIntent.putExtra(Course.COURSE_TITLE,courseList.getCourseTitle());
        setIntent.putExtra(Course.COURSE_CREATED, courseList.getCourseCreated());


        startActivityForResult(this.setIntent,REQUEST_CODE);
    }

     /**
     * on long click, delete course from assigned course list
     * @param parent this Activity
     * @param view - the ListView
     * @param position -position selected
     * @param id - id of position
     * @return true / false for row deleted
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //Need to check and see if this course is assigned to mentors, and terms before deletion.
        int deletedRowId = 0;
        Log.d(CLASS_NAME,"Delete course assignment.");
        CourseList courseAssignmentToRemove = (CourseList) this.courseListView.getItemAtPosition(position);

        boolean deletedCourse = false;
        //CHECK FOR ASSIGNED MENTOR AND DELETE
        if (mentorAssignmentsRepo.readCourseAssignmentExists(courseAssignmentToRemove)) {
            if (mentorAssignmentsRepo.delete(courseAssignmentToRemove) > 0)
            {
                deletedCourse = courseRepo.delete(courseAssignmentToRemove) > 0;
            }
        }
        else
        {
            deletedCourse = courseRepo.delete(courseAssignmentToRemove) > 0;
        }

        refreshListView();
        Log.d(CLASS_NAME,"Course deleted: " + deletedCourse);
        commonFunctions.makeToast("Course " + courseAssignmentToRemove.getCourseTitle() + " deleted.");

        return deletedCourse;
    }

    /**
     * onActivityResult ()
     * @param requestCode - code of calling app
     * @param resultCode - result code from app
     * @param data - bundled data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CourseSelection.REQUEST_CODE && resultCode == RESULT_OK)
        {
            Log.d(CLASS_NAME,"Results: " + data.getCategories());
        }
        refreshListView();
    }
}
