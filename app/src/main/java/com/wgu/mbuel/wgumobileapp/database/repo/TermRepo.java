package com.wgu.mbuel.wgumobileapp.database.repo;

/**
 * Created by mbuel on 4/15/2017.
 */
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.database.List.TermList;
import com.wgu.mbuel.wgumobileapp.database.WguDatabaseManager;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.Term;

import java.util.ArrayList;

public class TermRepo {
    //SQL to create table
    public static final String TABLE_TERM_CREATE =
        "CREATE TABLE " + Term.TABLE_TERM + " (\n" +
            Term.TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            Term.TERM_TITLE + " TEXT, \n" +
            Term.TERM_START + " TEXT, \n" +
            Term.TERM_END + " TEXT, \n" +
            Term.TERM_CREATED + " TEXT default CURRENT_TIMESTAMP \n" +
            ");";
    private final String CLASS_NAME = this.getClass().getName();

    private CommonFunctions commonFunctions = new CommonFunctions();
    private Term term;

    /**
     * TermRepo instantiation
     */
    public TermRepo()
    {
        term = new Term();
    }

    /**
     * create Term in table
     * @param term to create
     * @return row inserted
     */
    public int create(Term term){

        int addedRowId = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

        try {

            if (readTermTitleExists(term.getTermTitle())) {
                addedRowId = (int) db.insert(Term.TABLE_TERM, null, getTermContentValues(term));
            }
        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in creating a new entry: " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }
        return addedRowId;

    }


    /**
     * readTermlist reads all terms in table
     * @return ArrayList of TermList object
     */
    public ArrayList<TermList> readTermList()
    {
        TermList termItem;
        ArrayList<TermList> termList = new ArrayList<>();
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();

        try {

            Cursor cursor = db.query(Term.TABLE_TERM,Term.ALL_TERM_COLUMNS,null,null,null,null,null,null);
            while(cursor.moveToNext())
            {

                termItem = new TermList();
                termItem.setTermTitle(cursor.getString(cursor.getColumnIndex(Term.TERM_TITLE)));
                termItem.setTermId(cursor.getString(cursor.getColumnIndex(Term.TERM_ID)));
                termItem.setTermCreated(cursor.getString(cursor.getColumnIndex(Term.TERM_CREATED)));
                termItem.setTermStart(cursor.getString(cursor.getColumnIndex(Term.TERM_START)));
                termItem.setTermEnd(cursor.getString(cursor.getColumnIndex(Term.TERM_END)));

                termList.add(termItem);

            }

        }
        catch(SQLiteException sQe)
        {
            Log.e(this.CLASS_NAME,"Error in reading list entry: " + "\n" + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();

        }
        return termList;
    }

    /**
     * update Term - updates term in table
     * @param term to update
     * @return number of affected rows
     */
    public int update(Term term){
        int affectedRowId = 0;

        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            affectedRowId = db.update(
                    Term.TABLE_TERM,
                    getTermContentValues(term),
                    Term.TERM_ID + " = " + term.getTermId(),
                    null);
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
     * readTermTitleExists - prevent duplicate term title entry
     * @param termTitle to look for
     * @return true if it's NOT in the table, false if it is.
     */
    private boolean readTermTitleExists(String termTitle)
    {
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        String getTermTitle = "";

        String query = "SELECT \n" +
                Term.TERM_TITLE +
                " FROM \n " +
                Term.TABLE_TERM + "\n " +
                " WHERE \n " +
                Term.TERM_TITLE + " = '" + termTitle + "';";

        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                getTermTitle = cursor.getString(cursor.getColumnIndex(Term.TERM_TITLE));
            }
        } catch (SQLiteException sQe) {
            Log.e(CLASS_NAME, "Error in SQL. " + sQe.getMessage());
        }
        finally {
            WguDatabaseManager.getInstance().closeDatabase();
        }


        return (getTermTitle.isEmpty());
    }

    /**
     * delete selected row (Term object)
     * @param term object passed in, get's ID of selected row from this.
     * @return deleted row id
     */
    public int delete(Term term){
        int deletedRow = 0;
        SQLiteDatabase db = WguDatabaseManager.getInstance().openDatabase();
        try
        {
            deletedRow = db.delete(Term.TABLE_TERM,Term.TERM_ID + " = " +  term.getTermId(),null);
        }
        catch(SQLiteException sQe){
            Log.e(CLASS_NAME,"Problem deleting row." +"\n" + sQe.getMessage());
        }
        return deletedRow;

    }

    /**
     * termContent - construct ContentValues object from Term object data.
     * @param term object with it's data.
     * @return values (ContentValues object) to be used to insert or update database.
     */
    private ContentValues getTermContentValues(Term term)
    {
        ContentValues values = new ContentValues();
        values.put(Term.TERM_TITLE, term.getTermTitle());
        values.put(Term.TERM_START, term.getTermStart());
        values.put(Term.TERM_END, term.getTermEnd());

        return values;

    }
}
