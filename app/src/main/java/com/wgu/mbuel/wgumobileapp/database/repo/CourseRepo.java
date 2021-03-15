package com.wgu.mbuel.wgumobileapp.database.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.database.List.CourseList;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.List.TermList;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.CourseDetails;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.Term;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/15/2017.
 */

public class CourseRepo {


    //SQL to create table
    public static final String COURSE_TABLE_CREATE =
        "CREATE TABLE " + Course.TABLE_COURSE + " ( \n" +
            Course.COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            Course.COURSE_TITLE + " TEXT, \n" +
            Course.COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP \n" +
            ")";

    private final String CLASS_NAME = this.getClass().getName();
    private Course course;

    /**
     * instantiate CourseRepo, create Course object reference.
     */
    public CourseRepo()
    {
        course = new Course();
    }

    /**
     * create course in table
     * @param course - course to create
     * @return integer of added row ID
     */
    public int create(Course course){
        int addedRowId = 0;
        try {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();

            values.put(Course.COURSE_TITLE, course.getCourseTitle());

            addedRowId =  (int) db.insert(Course.TABLE_COURSE, null, values);
        }
        catch (SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in creating a new entry: " + "\n" + sQe.getMessage());
        }
        finally
        {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return addedRowId;

    }

    /**
     * readCourseExists - checks to see if course exists in the table.
     * @param courseToFind - course to search for in the table
     * @return true or false for course exists?
     */
    public boolean readCourseExists(Course courseToFind)
    {
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        //boolean mentorNameExists = false;
        String courseTitle = "";
        //int rowToMatch = 0;

        String query = "SELECT \n" +
                Course.COURSE_TITLE +
                " FROM \n " +
                Course.TABLE_COURSE + "\n " +
                " WHERE \n " +
                Course.COURSE_TITLE + " = '" + courseToFind.getCourseTitle() + "';";

        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                courseTitle = cursor.getString(cursor.getColumnIndex(Course.COURSE_TITLE));
            }
        } catch (SQLiteException sQe) {
            Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }


        return (courseTitle.isEmpty());
    }

    /**
     * readCourseRowCount - return number of rows in the table.
     * @return integer count of number of rows.
     */
    public int readCourseRowCount(){
        int rowCount = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            Cursor cursor = db.query(
                    Course.TABLE_COURSE,
                    Course.ALL_COURSE_COLUMNS,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            rowCount = cursor.getCount();
        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in getting count in Course table. " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return rowCount;
    }

    /**
     * readCourseList() - reads course table and returns list of courses
     * @return ArrayList of courses
     */
    public ArrayList<CourseList> readCourseList()
    {
        CourseList courseItem;
        ArrayList<CourseList> courseLists = new ArrayList<>();
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

        try {

            Cursor cursor = db.query(Course.TABLE_COURSE,Course.ALL_COURSE_COLUMNS,null,null,null,null,null,null);
            while(cursor.moveToNext())
            {

                courseItem = new CourseList();
                courseItem.setCourseId(cursor.getString(cursor.getColumnIndex(Course.COURSE_ID)));
                courseItem.setCourseTitle(cursor.getString(cursor.getColumnIndex(Course.COURSE_TITLE)));
                courseItem.setCourseCreated(cursor.getString(cursor.getColumnIndex(Course.COURSE_CREATED)));

                courseLists.add(courseItem);

            }

        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in reading list entry: " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return courseLists;
    }

    /**
     * update course
     * @param course course object to update
     * @return number of rows affected
     */
    public int update(Course course){
        int affectedRowId = 0;

        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            affectedRowId = db.update(Course.TABLE_COURSE,getCourseContentValues(course),Course.COURSE_ID + " = " + course.getCourseId(),null);

        }
        catch(SQLiteException sQe){
            Log.d(CLASS_NAME,"Problem updating Course. " + "\n" + sQe.getMessage());
        }
        return affectedRowId;
    }

    /**
     * getCourseContentvalues from Course object
     * @param course course object value to convert
     * @return ContentValues from Course
     */
    private ContentValues getCourseContentValues(Course course) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Course.COURSE_TITLE,course.getCourseTitle());

        return contentValues;
    }

    /**
     * delete - Course from table
     * @param course to delete
     * @return number of rows affected
     */
    public int delete(CourseList course){
        int deletedRow = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            deletedRow = db.delete(Course.TABLE_COURSE,Course.COURSE_ID + " = " + course.getCourseId(),null);
        }
        catch(SQLiteException sQe){
            Log.e(CLASS_NAME,"Problem deleting row." +"\n" + sQe.getMessage());
        }
        Log.d(CLASS_NAME,"Course Id to delete: " + course.getCourseId());
        Log.d(CLASS_NAME,"Row Deleted: " + deletedRow);
        return deletedRow;

    }
}
