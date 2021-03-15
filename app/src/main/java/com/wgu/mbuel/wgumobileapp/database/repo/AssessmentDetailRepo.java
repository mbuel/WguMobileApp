package com.wgu.mbuel.wgumobileapp.database.repo;

/**
 * Created by MBUEL on 4/15/2017.
 * CRUD support class for Assessment Details
 */
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.ImageProcessor;
import com.wgu.mbuel.wgumobileapp.database.model.AssessmentDetails;
import com.wgu.mbuel.wgumobileapp.database.List.AssessmentDetailList;

import java.util.ArrayList;
import java.util.List;

public class AssessmentDetailRepo {

    //SQL to create ASSESSMENT_DETAIL table
    public static final String TABLE_ASSESSMENT_DETAIL_CREATE =
        "CREATE TABLE " + AssessmentDetails.TABLE_ASSESSMENT_DETAIL + " ( \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_TITLE + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID + " INTEGER, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_ALARM + " INTEGER, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_TYPE + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_NOTES + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_STATUS + " TEXT, \n" +
            AssessmentDetails.ASSESSMENT_DETAIL_CREATED + " TEXT default CURRENT_TIMESTAMP \n" +
            ");";

    private final String CLASS_NAME = this.getClass().getName();
    private CommonFunctions commonFunctions = new CommonFunctions();
    private AssessmentDetails assessmentDetail;
    public AssessmentDetailRepo()
    {
        assessmentDetail = new AssessmentDetails();
    }

    /**
     * insert for AssessmentDetail - takes an assessmentDetail object (which aligns it with the data in the database) then stores it in the database.
     * @param assessmentDetail
     * @return row id of new entry - may be needed when doing multiple inserts for relation
     */
    public int create(AssessmentDetailList assessmentDetail)
    {
        int addedRowId = 0;
        try {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

            addedRowId = (int) db.insert(AssessmentDetails.TABLE_ASSESSMENT_DETAIL, null, updateValues(assessmentDetail));
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
     * updateValues - create ContentValues from AssessmentDetailList object
     * @param selectedAssessment - AssessmentDetailList object
     * @return ContentValues object
     */
    private ContentValues updateValues(AssessmentDetailList selectedAssessment)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_TITLE, selectedAssessment.getAssessmentDetailTitle());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID, selectedAssessment.getAssessmentDetailCourseDetailId());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_CREATED, selectedAssessment.getAssessmentDetailCreated());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE, selectedAssessment.getAssessmentDetailDueDate());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_ALARM, selectedAssessment.getAssessmentDetailAlarm());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE, selectedAssessment.getAssessmentDetailCompletionDate());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_TYPE, selectedAssessment.getAssessmentDetailType());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH, selectedAssessment.getAssessmentDetailPhotoPath());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_NOTES,selectedAssessment.getAssessmentDetailNotes());
        contentValues.put(AssessmentDetails.ASSESSMENT_DETAIL_STATUS,selectedAssessment.getAssessmentDetailStatus());

        return contentValues;
    }

    /**
     * readRowExist - takes AssessmentDetail object and checks to see if any course assignments exist based on the courseDetailsId
     * @param courseToCount - AssessmentDetailList object with id of courseDetail
     * @return true if rows exist, false if they don't.
     */
    public boolean readRowExists(AssessmentDetailList courseToCount){

        int courseDetailId = (commonFunctions.tryParseInt(courseToCount.getAssessmentDetailCourseDetailId())?commonFunctions.getInteger():0);

        int rowCount = 0;

        if (courseDetailId > 0) {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            String query = "SELECT \n" +
                    AssessmentDetails.ASSESSMENT_DETAIL_ID +
                    " FROM \n " +
                    AssessmentDetails.TABLE_ASSESSMENT_DETAIL + "\n " +
                    " WHERE \n " +
                    AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID  + " = " + courseDetailId + ";";

            String mentorName = "";
            try {
                Cursor cursor = db.rawQuery(query, null);
                rowCount = cursor.getCount();
                Log.d(CLASS_NAME, "row(s) exists count: " + rowCount + " \n " + "mentorId: " + courseDetailId);

            }
            catch (SQLiteException sQe) {
                Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
            }
            finally
            {
                WguDatabaseManager.getInstance().closeDatabase();
            }

        }
        return rowCount > 0;


    }

    /**
     * readAssessmentDetailList() - returns ArrayList object of AssessmentDetailList object
     * @param course TermDetailList object to locate Assessments assigned
     * @return ArrayList of AssessmentDetailList
     */
    public ArrayList<AssessmentDetailList> readAssessmentDetailList(TermDetailList course){
        AssessmentDetailList assessmentDetailList = new AssessmentDetailList();
        ArrayList<AssessmentDetailList> assessmentDetailLists = new ArrayList<>();

        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        int courseDetailsId = commonFunctions.tryParseInt(course.getCourseDetailsId())?commonFunctions.getInteger():0;

        if (courseDetailsId > 0) {
            try {
                Cursor cursor = db.query(
                        AssessmentDetails.TABLE_ASSESSMENT_DETAIL,
                        AssessmentDetails.ALL_ASSESSMENT_DETAIL_COLUMNS,
                        AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID + " = " + courseDetailsId,
                        null,
                        null,
                        null,
                        null);
                while (cursor.moveToNext()) {
                    assessmentDetailList = new AssessmentDetailList();
                    assessmentDetailList.setAssessmentDetailId(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_ID)));
                    assessmentDetailList.setAssessmentDetailCourseDetailId(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID)));
                    assessmentDetailList.setAssessmentDetailTitle(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_TITLE)));
                    assessmentDetailList.setAssessmentDetailAlarm(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_ALARM)));
                    assessmentDetailList.setAssessmentDetailCompletionDate(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE)));
                    assessmentDetailList.setAssessmentDetailDueDate(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE)));
                    Log.d(CLASS_NAME,"Due Date: " + assessmentDetailList.getAssessmentDetailDueDate());
                    assessmentDetailList.setAssessmentDetailNotes(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_NOTES)));
                    assessmentDetailList.setAssessmentDetailPhotoPath(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH)));
                    assessmentDetailList.setAssessmentDetailStatus(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_STATUS)));
                    assessmentDetailList.setAssessmentDetailType(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_TYPE)));
                    assessmentDetailList.setAssessmentDetailCreated(cursor.getString(cursor.getColumnIndex(AssessmentDetails.ASSESSMENT_DETAIL_CREATED)));
                    assessmentDetailLists.add(assessmentDetailList);
                }
            } catch (SQLiteException sQe) {
                Log.e(this.CLASS_NAME, "Error in updating existing entry: " + "\n" + sQe.getMessage());
            } finally {
                WguDatabaseManager.getInstance().closeDatabase();
            }
        }
        return assessmentDetailLists;

    }

    /**
     * update Assessment
     * @param selectedAssessment - AssessmentDetailList to return
     * @return integer of NUMBER of updated rows
     */
    public int update(AssessmentDetailList selectedAssessment){
        int updatedRowId = 0;
        try {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            updatedRowId = db.update(
                AssessmentDetails.TABLE_ASSESSMENT_DETAIL,
                updateValues(selectedAssessment),
                AssessmentDetails.ASSESSMENT_DETAIL_ID + " = " + selectedAssessment.getAssessmentDetailId(), null);
        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in updating existing entry: " + "\n" + sQe.getMessage());
        }
        finally
        {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return updatedRowId;
    }

    /**
     * delete Assessment
     * @param assessmentDetail - Assessment to delete
     * @return integer of NUMBER of affected rows.
     */
    public int delete(AssessmentDetailList assessmentDetail)
    {
        int rowsDeleted = 0;
        try {
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            if (!assessmentDetail.getAssessmentDetailPhotoPath().equals(""))
            {
                //DELETE PHOTO
                ImageProcessor deletePhoto = new ImageProcessor(App.getContext());
                Log.d(CLASS_NAME,"Photo Deleted: " + deletePhoto.deleteFile(assessmentDetail.getAssessmentDetailPhotoPath()));
            }
            Log.d(CLASS_NAME,"Assessment to delete: " + assessmentDetail.getAssessmentDetailId());
            rowsDeleted = db.delete(
                    AssessmentDetails.TABLE_ASSESSMENT_DETAIL,
                    AssessmentDetails.ASSESSMENT_DETAIL_ID + " = " + assessmentDetail.getAssessmentDetailId(),
                    null);
        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error deleting an entry: " + "\n" + sQe.getMessage());
        }
        finally
        {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return rowsDeleted;

    }
    public int deleteCourse(AssessmentDetailList assessmentDetail){
        int rowsDeleted = 0;
        try{
            SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
            if (!assessmentDetail.getAssessmentDetailPhotoPath().equals(""))

            Log.d(CLASS_NAME,"Assessment to delete: " + assessmentDetail.getAssessmentDetailId());
            rowsDeleted = db.delete(
                    AssessmentDetails.TABLE_ASSESSMENT_DETAIL,
                    AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID + " = " + assessmentDetail.getAssessmentDetailCourseDetailId(),
                    null);
        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error deleting an entry: " + "\n" + sQe.getMessage());
        }
        finally
        {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return rowsDeleted;
    }



}