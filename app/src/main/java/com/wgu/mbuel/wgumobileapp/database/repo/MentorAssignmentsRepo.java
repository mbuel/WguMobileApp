package com.wgu.mbuel.wgumobileapp.database.repo;

/**
 * Created by mbuel on 4/15/2017.
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.database.List.CourseList;
import com.wgu.mbuel.wgumobileapp.database.List.MentorAssignmentList;
import com.wgu.mbuel.wgumobileapp.database.List.MentorList;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.MentorAssignments;

import java.util.ArrayList;

public class MentorAssignmentsRepo {
    private static final String JOIN_QUERY = "SELECT \n" +
            Course.TABLE_COURSE + "." + Course.COURSE_TITLE + ", \n" +
            Mentor.TABLE_MENTOR + "." + Mentor.MENTOR_NAME  + ",\n" +
            MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_ID + ",\n" +
            MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + ",\n" +
            MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID + ",\n" +
            MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENT_CREATED_DATE + "\n" +
            "FROM \n" +
            Course.TABLE_COURSE + " \n" +
            "\t INNER JOIN " + MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + " \n" +
            "\t\t ON " + Course.TABLE_COURSE + "." + Course.COURSE_ID + " = " + MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID + " \n" +
            "\t INNER JOIN " + Mentor.TABLE_MENTOR + "\n" +
            "\t\t ON " + MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + " = " + Mentor.TABLE_MENTOR + "." + Mentor.MENTOR_ID  +  " \n" +
            "WHERE \n";

    private Cursor cursor;
    private CommonFunctions commonFunctions = new CommonFunctions();
    //SQL to create table
    public static final String TABLE_MENTOR_ASSIGNMENT_CREATE =
        "CREATE TABLE " + MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + " ( \n" +
            MentorAssignments.MENTOR_ASSIGNMENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + " INTEGER, \n" +
            MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID + " INTEGER, \n" +
            MentorAssignments.MENTOR_ASSIGNMENT_CREATED_DATE + " TEXT default CURRENT_TIMESTAMP \n" +
            ");";
    private final String CLASS_NAME = this.getClass().getName();

    private MentorAssignments mentorAssignments;
    //private Mentor mentor = new Mentor();
    private MentorRepo mentorRepo = new MentorRepo();
    //private Course course = new Course();
    private CourseRepo courseRepo = new CourseRepo();

    /**
     * instantiate MentorAssignmentsRepo
     */
    public MentorAssignmentsRepo()
    {
        mentorAssignments = new MentorAssignments();
    }


    /**
     * create mentorAssignment - creates a mentor to course or course to mentor assignment
     * @param mentorAssignments mentor assignment to add to table
     * @return inserted row ID
     */
    public int create(MentorAssignments mentorAssignments){
        int addedRowId = 0;
        try {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();

            values.put(MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID, mentorAssignments.getMentorAssignmentsCourseId());
            values.put(MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID, mentorAssignments.getMentorAssignmentsMentorId());

            return (int) db.insert(MentorAssignments.TABLE_MENTOR_ASSIGNMENTS, null, values);
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
     * readMentoDetailsCourseAssignments
     * @param mentorIdFilter mentorId to filter assignment list to
     * @return ArrayList of MentorAssignments filtered by mentor
     */
    public ArrayList<MentorAssignmentList> readMentorDetailsCourseAssignments(String mentorIdFilter){
        String query = this.JOIN_QUERY +
                MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + " = " + (commonFunctions.tryParseInt(mentorIdFilter)?commonFunctions.getInteger():0)  + ";";

        return readMentorAssignmentsList(query);

    }

    /**
     * readCourseDetailsMentorAssignments
     * @param courseIdFilter courseId to filter assignment list to
     * @return ArrayList of MentorAssignments filtered by course
     */
    public ArrayList<MentorAssignmentList> readCourseDetailsMentorAssignments(String courseIdFilter){
        String query = this.JOIN_QUERY +
                MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "." + MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID + " = " + (commonFunctions.tryParseInt(courseIdFilter)?commonFunctions.getInteger():0)  + ";";
        Log.d(CLASS_NAME,"SQL query: " + query);
        return readMentorAssignmentsList(query);

    }

    /** readMentorAssignmentsList - helper class for readCourseDetailsMentorAssignments and readMentorDetailsCourseAssignments
     * returns ArrayList of mentorassignments
     */
    private ArrayList<MentorAssignmentList> readMentorAssignmentsList(String query){
        MentorAssignments mentorAssignments;
        MentorAssignmentList mentorRow;
        ArrayList<MentorAssignmentList> mentorList = new ArrayList<>();


        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

        try {

            cursor = db.rawQuery(query,null);
            while(cursor.moveToNext())
            {
                mentorRow = new MentorAssignmentList();
                mentorRow.setMentorAssignmentsId(cursor.getString(cursor.getColumnIndex(MentorAssignments.MENTOR_ASSIGNMENTS_ID)));
                mentorRow.setMentorAssignmentsMentorId(cursor.getString(cursor.getColumnIndex(MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID)));
                mentorRow.setMentorName(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_NAME)));
                mentorRow.setMentorAssignmentsCourseId(cursor.getString(cursor.getColumnIndex(MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID)));
                mentorRow.setCourseTitle(cursor.getString(cursor.getColumnIndex(Course.COURSE_TITLE)));
                mentorRow.setMentorAssignmentCreatedDate(cursor.getString(cursor.getColumnIndex(MentorAssignments.MENTOR_ASSIGNMENTS_ID)));

                mentorList.add(mentorRow);

            }

        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in reading list entry: " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return mentorList;
    }

    /**
     * readMentorAssignmentExists - takes mentor and CourseList object to determine if this course has already been assigned
     * @param mentor mentor object
     * @param courseList courseList object
     * @return integer value (0 or 1)
     */
    public boolean readMentorAssignmentExists(Mentor mentor, CourseList courseList){
        {
            int mentorId = 0;
            int courseId = 0;
            int rowCount = 0;
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            if (commonFunctions.tryParseInt(mentor.getMentorId()))
            {
                 mentorId = commonFunctions.getInteger();
            }
            if (commonFunctions.tryParseInt(courseList.getCourseId()))
            {
                courseId = commonFunctions.getInteger();
            }

            if (courseId > 0 && mentorId > 0) {
                String query = "SELECT \n" +
                        MentorAssignments.MENTOR_ASSIGNMENTS_ID +
                        " FROM \n " +
                        MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "\n " +
                        " WHERE \n " +
                        MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + " = " + mentorId + " AND \n " +
                        MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID + " = " + courseId + ";";

                String mentorName = "";
                try {
                    Cursor cursor = db.rawQuery(query, null);
                    rowCount = cursor.getCount();
                    Log.d(CLASS_NAME, "row exists count: " + rowCount + " \n " + "mentorId: " + mentorId + " \n " + "courseId: " + courseId);

                } catch (SQLiteException sQe) {
                    Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
                }
                finally {
                    WguDatabaseManager.getInstance().closeDatabase();
                }

            }
            return rowCount > 0;
        }

    }

    /**
     * readCourseAssignmentExists - makes sure duplicate assignments cannot be made
     * @param course course to check for
     * @return - true or false - does assignment exist?
     */
    public boolean readCourseAssignmentExists(CourseList course)
    {
        int courseId = (commonFunctions.tryParseInt(course.getCourseId())?commonFunctions.getInteger():0);

        int rowCount = 0;
        if (courseId > 0) {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            String query = "SELECT \n" +
                    MentorAssignments.MENTOR_ASSIGNMENTS_ID +
                    " FROM \n " +
                    MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "\n " +
                    " WHERE \n " +
                    MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + " = " + courseId + ";";

            try {
                Cursor cursor = db.rawQuery(query, null);
                rowCount = cursor.getCount();
                Log.d(CLASS_NAME, "Row exists count: " + rowCount + " \n " + "courseId: " + courseId);

            } catch (SQLiteException sQe) {
                Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
            }
            finally {
                WguDatabaseManager.getInstance().closeDatabase();
            }
        }

        return rowCount > 0;
    }
    /**
     * readMentorAssignmentExists - takes mentor and CourseList object to determine if this course has already been assigned
     * @param mentor mentor object
     * @return integer value (0 or 1)
     */
    public boolean readMentorAssignmentExists(Mentor mentor){

        int mentorId = (commonFunctions.tryParseInt(mentor.getMentorId())?commonFunctions.getInteger():0);

        int rowCount = 0;
        if (mentorId > 0) {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            String query = "SELECT \n" +
                    MentorAssignments.MENTOR_ASSIGNMENTS_ID +
                    " FROM \n " +
                    MentorAssignments.TABLE_MENTOR_ASSIGNMENTS + "\n " +
                    " WHERE \n " +
                    MentorAssignments.MENTOR_ASSIGNMENTS_MENTOR_ID + " = " + mentorId + ";";

            String mentorName = "";
            try {
                Cursor cursor = db.rawQuery(query, null);
                rowCount = cursor.getCount();
                Log.d(CLASS_NAME, "Row exists count: " + rowCount + " \n " + "mentorId: " + mentorId);

            } catch (SQLiteException sQe) {
                Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
            }
            finally {
                WguDatabaseManager.getInstance().closeDatabase();
            }
        }

        return rowCount > 0;


    }

    /**
     * delete assignment from table
     * @param assignmentToDelete - mentor assignment to delete
     * @return number of rows deleted
     */
    public int delete(MentorAssignmentList assignmentToDelete) {

        int deletedRow = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try {
            deletedRow = db.delete(
                    MentorAssignments.TABLE_MENTOR_ASSIGNMENTS,
                    MentorAssignments.MENTOR_ASSIGNMENTS_ID + " = " + assignmentToDelete.getMentorAssignmentsId(),
                    null);
        } catch (SQLiteException sQe) {
            Log.e(CLASS_NAME, "Problem deleting row." + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return deletedRow;
    }

    /**
     * delete courseList object to delete course assignment from table
     * @param courseToDelete
     * @return number of rows deleted
     */
    public int delete (CourseList courseToDelete)
    {
        int deletedRow = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            deletedRow = db.delete(
                    MentorAssignments.TABLE_MENTOR_ASSIGNMENTS,
                    MentorAssignments.MENTOR_ASSIGNMENTS_COURSE_ID + " = " + courseToDelete.getCourseId(),
                    null);
        }
        catch (SQLiteException sQe) {
            Log.e(CLASS_NAME, "Problem deleting row." + "\n" + sQe.getMessage());
        }
            finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }

        return deletedRow;

    }
}
