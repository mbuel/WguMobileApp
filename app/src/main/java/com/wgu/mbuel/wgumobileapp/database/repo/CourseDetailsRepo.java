package com.wgu.mbuel.wgumobileapp.database.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.CourseDetails;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/15/2017.
 */

public class CourseDetailsRepo {


    public static final String TABLE_COURSE_DETAILS_CREATE =
        "CREATE TABLE " + CourseDetails.TABLE_COURSE_DETAILS + " ( \n" +
            CourseDetails.COURSE_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            CourseDetails.COURSE_DETAILS_COURSE_ID + " INTEGER, \n" +
            CourseDetails.COURSE_DETAILS_TERM_ID + " INTEGER, \n" +
            CourseDetails.COURSE_DETAILS_NOTES + " TEXT, \n" +
            CourseDetails.COURSE_DETAILS_START_DATE + " TEXT, \n" +
            CourseDetails.COURSE_DETAILS_START_ALARM + " INTEGER, \n" +
            CourseDetails.COURSE_DETAILS_END_DATE + " TEXT, \n" +
            CourseDetails.COURSE_DETAILS_END_ALARM + " INTEGER, \n" +
            CourseDetails.COURSE_DETAILS_STATUS + " TEXT, \n" +
            CourseDetails.COURSE_DETAILS_CREATED + " TEXT default CURRENT_TIMESTAMP \n" +
            ");";

    private final String CLASS_NAME = this.getClass().getName();
    private CourseDetails courseDetails;
    private SQLiteDatabase database;
    private CommonFunctions commonFunctions = new CommonFunctions();

    /**
     * CourseDetailsRepo instantiation - created CourseDetails object
     */
    public CourseDetailsRepo()
    {
        courseDetails = new CourseDetails();
    }

    /**
     * create - creates Course Details assignment to Term.
     * @param createTermAssignment - Course object to assign to Term.
     * @return integer of created row
     */
    public int create(TermDetailList createTermAssignment){
        int addedRowId = 0;
        Log.d(CLASS_NAME,"COURSE ID: " + createTermAssignment.getCourseDetailsCourseId());

        database = WguDatabaseManager.getInstance().openDatabase();

        try {
            addedRowId = (int) database.insert(CourseDetails.TABLE_COURSE_DETAILS, null, convertTermToContent(createTermAssignment));
        }
        catch (SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in creating a new entry: " + "\n" + sQe.getMessage());
        }
        finally
        {
            if (database.isOpen()) {
                WguDatabaseManager.getInstance().closeDatabase();
            }
        }
        return addedRowId;
    }

    /**
     * readTermDetailArrayList - Return ArrayList of assigned Courses to Term.
     * @param selectedTermId - termId to filter on
     * @return ArrayList of TermDetailList object
     */
    public ArrayList<TermDetailList> readTermDetailArrayList(int selectedTermId){
        TermDetailList termDetailRow;
        ArrayList<TermDetailList> termDetailLists = new ArrayList<>();
        int addedRowId = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        Cursor cursor = null;
        try {


            String query = "SELECT \n" +
                    Course.TABLE_COURSE + ".`" + Course.COURSE_TITLE + "`, \n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_ID + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_COURSE_ID + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_TERM_ID + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_NOTES + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_START_DATE + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_START_ALARM + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_END_DATE + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_END_ALARM + "`,\n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_STATUS + "`\n" +
                "FROM \n" +
                    Course.TABLE_COURSE + " \n" +
                "JOIN \n" +
                    CourseDetails.TABLE_COURSE_DETAILS + " \n" +
                "ON \n" +
                    Course.TABLE_COURSE + ".`" + Course.COURSE_ID + "` = " + CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_COURSE_ID + "` \n" +
                "WHERE \n" +
                    CourseDetails.TABLE_COURSE_DETAILS + ".`" + CourseDetails.COURSE_DETAILS_TERM_ID + "` = " + selectedTermId +";";
             cursor = db.rawQuery(query,null);

            while(cursor.moveToNext())
            {


                termDetailRow = new TermDetailList();
                termDetailRow.setCourseTitle(cursor.getString(cursor.getColumnIndex(Course.COURSE_TITLE)));
                termDetailRow.setCourseDetailsCourseId(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_COURSE_ID)));
                termDetailRow.setCourseDetailsId(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_ID)));
                termDetailRow.setCourseDetailsTermId(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_TERM_ID)));
                termDetailRow.setCourseDetailsStartDate(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_START_DATE)));
                termDetailRow.setCourseDetailsStartAlarm(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_START_ALARM)));
                termDetailRow.setCourseDetailsEndDate(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_END_DATE)));
                termDetailRow.setCourseDetailsEndAlarm(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_END_ALARM)));
                termDetailRow.setCourseDetailsStatus(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_STATUS)));
                termDetailRow.setCourseDetailsNotes(cursor.getString(cursor.getColumnIndex(CourseDetails.COURSE_DETAILS_NOTES)));

                termDetailLists.add(termDetailRow);

            }

            //addedRowId = db.qu .insert(Term.TABLE_TERM,null,values);

        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in reading list entry: " + "\n" + sQe.getMessage());
        }
        finally {
            try {
                WguDatabaseManager.getInstance().closeDatabase();
                if (cursor != null) {
                    cursor.close();
                }
            }
            catch(Exception e)
            {
                Log.e(CLASS_NAME,"Problem closing resources. " + e.getMessage());
            }


        }
        return termDetailLists;

    }

    /**
     * update - updates the table for Course Details
     * @param termDetailsToSave - object to limit the update to. Converts the CourseDetailsId and limits update to that row.
     * @return int number of rows updated
     */
    public int update(TermDetailList termDetailsToSave){
        int affectedRowId = 0;
        int courseDetailsId = (commonFunctions.tryParseInt(termDetailsToSave.getCourseDetailsId())?commonFunctions.getInteger():0);
        Log.d(CLASS_NAME,"converted CourseDetailsID: " + courseDetailsId);

        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            affectedRowId = db.update(
                CourseDetails.TABLE_COURSE_DETAILS,
                convertTermToContent(termDetailsToSave),
                CourseDetails.COURSE_DETAILS_ID + " = " + courseDetailsId,
                null);
        }
        catch(SQLiteException sQe){
            Log.d(CLASS_NAME,"Problem updating Term Details. " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return affectedRowId;
    }

    /**
     * delete - deletes TermDetailList assigned course
     * @param termDetailsToDelete Course assignment to delete
     * @return integer of number of rows affected
     */
    public int delete(TermDetailList termDetailsToDelete){
        int deletedRow = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            int courseDetailsId = (commonFunctions.tryParseInt(termDetailsToDelete.getCourseDetailsId())?commonFunctions.getInteger():0);
            Log.d(CLASS_NAME,"TRYING TO DELETE COURSE DETAILS ID: " + courseDetailsId);
            deletedRow = db.delete(
                CourseDetails.TABLE_COURSE_DETAILS,
                CourseDetails.COURSE_DETAILS_ID + " = " + courseDetailsId,
                null);
        }
        catch(SQLiteException sQe){
            Log.e(CLASS_NAME,"Problem deleting row." +"\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        Log.d(CLASS_NAME,"Deleted ROW: " + deletedRow);
        return deletedRow;
    }

    /**
     * convertTermToContent() - takes TermDetailList object and converts it to ContentValues
     * @param termToConvert - TermDetailList object to convert
     * @return ContentValues converted from TermDetailList
     */
    private ContentValues convertTermToContent(TermDetailList termToConvert)
    {
        ContentValues values = new ContentValues();

        int updateRowId = commonFunctions.tryParseInt(termToConvert.getCourseDetailsId())?commonFunctions.getInteger():0;

        if (updateRowId > 0)
        {
            values.put(CourseDetails.COURSE_DETAILS_ID,commonFunctions.getString());
        }

        values.put(CourseDetails.COURSE_DETAILS_COURSE_ID, termToConvert.getCourseDetailsCourseId());
        Log.d(CLASS_NAME,"COURSE DETAILS COURSE ID: " + termToConvert.getCourseDetailsCourseId());
        values.put(CourseDetails.COURSE_DETAILS_TERM_ID, termToConvert.getCourseDetailsTermId());
        values.put(CourseDetails.COURSE_DETAILS_START_DATE, termToConvert.getCourseDetailsStartDate());
        Log.d(CLASS_NAME,"COURSE DETAILS START DATE: " + termToConvert.getCourseDetailsStartDate());
        values.put(CourseDetails.COURSE_DETAILS_START_ALARM, termToConvert.getCourseDetailsStartAlarm());
        values.put(CourseDetails.COURSE_DETAILS_END_DATE, termToConvert.getCourseDetailsEndDate());
        Log.d(CLASS_NAME,"COURSE DETAILS END DATE: " + termToConvert.getCourseDetailsEndDate());
        values.put(CourseDetails.COURSE_DETAILS_END_ALARM, termToConvert.getCourseDetailsEndAlarm());
        Log.d(CLASS_NAME,"Notes in repo: " + termToConvert.getCourseDetailsNotes());
        Log.d(CLASS_NAME,"Status in repo: " + termToConvert.getCourseDetailsStatus());
        values.put(CourseDetails.COURSE_DETAILS_STATUS, termToConvert.getCourseDetailsStatus());
        values.put(CourseDetails.COURSE_DETAILS_NOTES,termToConvert.getCourseDetailsNotes());

        return values;
    }
}
