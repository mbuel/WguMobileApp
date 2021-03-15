package com.wgu.mbuel.wgumobileapp.database;
//Creating ContentProvider in NotesProvider class
//Assuming that we have DBOpenHelper class where all the columns and table names are defined
//04-08-2017 MBUEL - Modified DGASSNER's CODE for use in WGU project. Not putting this up to be copied, exactly putting it up as an example to future students in this class.
//THIS TABLE IS TO TRACK TERMS

//4-10-2017 REALIZED I NEED TO PLACE ALL THE TABLES IN ONE CLASS. REFACTORED

//4-15-2017 REFACTORED AGAIN after viewing example from TAN WOON HOW

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.model.AssessmentDetails;
import com.wgu.mbuel.wgumobileapp.database.model.Course;
import com.wgu.mbuel.wgumobileapp.database.model.CourseDetails;
import com.wgu.mbuel.wgumobileapp.database.model.Mentor;
import com.wgu.mbuel.wgumobileapp.database.model.MentorAssignments;
import com.wgu.mbuel.wgumobileapp.database.model.Term;
import com.wgu.mbuel.wgumobileapp.database.repo.AssessmentDetailRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseDetailsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorAssignmentsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.MentorRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.TermRepo;

public class DBwguHelper extends SQLiteOpenHelper {

    //Constants for database name and version
    public static final String WGU_DATABASE_NAME = "wguMobileApplication.db";
    private static final int DATABASE_VERSION = 13;

    //Constructor with call to super using our DATABASE_NAME and DATABASE_VERSION
    public DBwguHelper() {
        super(App.getContext(), WGU_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //We are executing table creating SQL on passed database
        sqLiteDatabase.execSQL(AssessmentDetailRepo.TABLE_ASSESSMENT_DETAIL_CREATE);
        sqLiteDatabase.execSQL(TermRepo.TABLE_TERM_CREATE);
        sqLiteDatabase.execSQL(MentorRepo.MENTOR_TABLE_CREATE);
        sqLiteDatabase.execSQL(CourseDetailsRepo.TABLE_COURSE_DETAILS_CREATE);
        sqLiteDatabase.execSQL(CourseRepo.COURSE_TABLE_CREATE);
        sqLiteDatabase.execSQL(MentorAssignmentsRepo.TABLE_MENTOR_ASSIGNMENT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //We should drop table if it exists
        //Recreate the table using current table structure
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AssessmentDetails.TABLE_ASSESSMENT_DETAIL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Course.TABLE_COURSE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CourseDetails.TABLE_COURSE_DETAILS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Mentor.TABLE_MENTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MentorAssignments.TABLE_MENTOR_ASSIGNMENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Term.TABLE_TERM);

        onCreate(sqLiteDatabase);
    }
}