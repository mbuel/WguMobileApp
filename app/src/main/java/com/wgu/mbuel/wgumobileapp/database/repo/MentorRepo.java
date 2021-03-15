package com.wgu.mbuel.wgumobileapp.database.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.database.List.MentorAssignmentList;
import com.wgu.mbuel.wgumobileapp.database.List.MentorList;
import com.wgu.mbuel.wgumobileapp.database.List.TermList;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.MentorAssignments;
import com.wgu.mbuel.wgumobileapp.database.model.Term;

import java.util.ArrayList;

/**
 * Created by mbuel on 4/15/2017.
 */

public class MentorRepo {



    //SQL to create table
    public static final String MENTOR_TABLE_CREATE =
        "CREATE TABLE " + Mentor.TABLE_MENTOR + " (\n" +
            Mentor.MENTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            Mentor.MENTOR_NAME + " TEXT, \n" +
            Mentor.MENTOR_HOME_PHONE_NUMBER + " TEXT, \n" +
            Mentor.MENTOR_CELL_PHONE_NUMBER + " TEXT, \n" +
            Mentor.MENTOR_WORK_EMAIL + " TEXT, \n" +
            Mentor.MENTOR_HOME_EMAIL + " TEXT, \n" +
            Mentor.MENTOR_CREATED + " TEXT default CURRENT_TIMESTAMP \n" +
            ");";

    private final String CLASS_NAME = this.getClass().getName();
    private CommonFunctions commonFunctions = new CommonFunctions();
    private Mentor mentor;

    /**
     * MentorRepo object to instantiate
     */
    public MentorRepo()
    {
        mentor = new Mentor();
    }

    /**
     * create mentor in mentor table
     * @param mentor to create
     * @return row inserted into table
     */
    public int create(Mentor mentor){

        int addedRowId = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

        try {

            addedRowId = (int) db.insert(Mentor.TABLE_MENTOR, null, getMentorContentValues(mentor));
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
     * readSelectedRow - reads row from table and returns it as a MentorList object
     * @param selectedRow mentor to find
     * @return MentorList object from found ID
     */
    public MentorList readSelectedRow(MentorAssignmentList selectedRow)
    {
        MentorList mentorList = null;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            Cursor cursor = db.query(Mentor.TABLE_MENTOR,Mentor.ALL_MENTOR_COLUMNS,Mentor.MENTOR_ID + " = " + selectedRow.getMentorAssignmentsMentorId(),null,null,null,null,null);

            while(cursor.moveToNext())
            {

                mentorList = new MentorList();
                mentorList.setMentorId(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_ID)));
                mentorList.setMentorName(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_NAME)));
                mentorList.setMentorHomePhoneNumber(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_HOME_PHONE_NUMBER)));
                mentorList.setMentorCellPhoneNumber(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_CELL_PHONE_NUMBER)));
                mentorList.setMentorWorkEmail(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_WORK_EMAIL)));
                mentorList.setMentorHomeEmail(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_HOME_EMAIL)));
                mentorList.setMentorCreated(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_CREATED)));

                //mentorLists.add(mentorDetails);

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
     * readMentorList() - returns list of Mentors from table
     * @return ArrayList of mentors from table
     */
    public ArrayList<MentorList> readMentorNameList()
    {
        MentorList mentorDetails;
        ArrayList<MentorList> mentorLists = new ArrayList<>();
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

        try {

            Cursor cursor = db.query(Mentor.TABLE_MENTOR,Mentor.ALL_MENTOR_COLUMNS,null,null,null,null,null,null);
            //mentorDetails = new MentorList();
//            if (!(cursor.isLast()))
//            {
//                mentorDetails.setMentorId("0");
//                mentorDetails.setMentorName("NONE");
//                mentorLists.add(mentorDetails);
//            }
            while(cursor.moveToNext())
            {

                mentorDetails = new MentorList();
                mentorDetails.setMentorId(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_ID)));
                mentorDetails.setMentorName(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_NAME)));
                mentorDetails.setMentorHomePhoneNumber(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_HOME_PHONE_NUMBER)));
                mentorDetails.setMentorCellPhoneNumber(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_CELL_PHONE_NUMBER)));
                mentorDetails.setMentorWorkEmail(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_WORK_EMAIL)));
                mentorDetails.setMentorHomeEmail(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_HOME_EMAIL)));
                mentorDetails.setMentorCreated(cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_CREATED)));

                mentorLists.add(mentorDetails);

            }

        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in reading list entry: " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();

        }
        return mentorLists;
    }

    /**
     * readMentorNameExists() - reads whether the name already exists in the table
     * @param mentorName mentor to look for
     * @return true if mentor name is not in the table
     */
    public boolean readMentorNameExists(String mentorName)
    {
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        String getMentorName = "";

        String query = "SELECT \n" +
                Mentor.MENTOR_NAME +
                " FROM \n " +
                Mentor.TABLE_MENTOR + "\n " +
                " WHERE \n " +
                Mentor.MENTOR_NAME + " = '" + mentorName + "';";

        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                getMentorName = cursor.getString(cursor.getColumnIndex(Mentor.MENTOR_NAME));
            }
        } catch (SQLiteException sQe) {
            Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }


        return (getMentorName.isEmpty());
    }


    /**
     * update - Mentor in table
     * @param mentor mentor to update
     * @return number of rows affected
     */
    public int update(Mentor mentor){
        int affectedRowId = 0;

        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            affectedRowId = db.update(Mentor.TABLE_MENTOR,getMentorContentValues(mentor),Mentor.MENTOR_ID + " = " + mentor.getMentorId(),null);
        }
        catch(SQLiteException sQe){
            Log.d(CLASS_NAME,"Problem updating Term. " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return affectedRowId;
    }

    /**
     * delete mentor from table
     * @param mentor to delete
     * @return numbers of rows affected
     */
    public int delete(Mentor mentor){
        int deletedRow = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            deletedRow = db.delete(Mentor.TABLE_MENTOR, Mentor.MENTOR_ID + " = " + mentor.getMentorId(),null);
        }
        catch(SQLiteException sQe){
            Log.e(CLASS_NAME,"Problem deleting row." +"\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        Log.d(CLASS_NAME,"Deleted Row from MentorRepo: " + deletedRow);
        return deletedRow;

    }

    /**
     * mentorRepo converts Mentor object into a ContentValues hash map
     * @param mentor - mentor object
     * @return ContentValues object
     */
    private ContentValues getMentorContentValues(Mentor mentor)
    {
        ContentValues values = new ContentValues();
        values.put(Mentor.MENTOR_NAME, mentor.getMentorName());
        values.put(Mentor.MENTOR_HOME_EMAIL, mentor.getMentorHomeEmail());
        values.put(Mentor.MENTOR_WORK_EMAIL, mentor.getMentorWorkEmail());
        values.put(Mentor.MENTOR_HOME_PHONE_NUMBER,mentor.getMentorHomePhoneNumber());
        values.put(Mentor.MENTOR_CELL_PHONE_NUMBER, mentor.getMentorCellPhoneNumber());

        return values;

    }


}
