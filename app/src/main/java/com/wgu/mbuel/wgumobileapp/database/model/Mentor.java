package com.wgu.mbuel.wgumobileapp.database.model;

/**
 * Created by MBUEL on 4/15/2017.
 * class for Mentor table structure
 */

public class Mentor {

    //Constants for identifying MENTOR table and columns
    public static final String TABLE_MENTOR = "mentor";
    public static final String MENTOR_ID = "_id";
    public static final String MENTOR_NAME = "mentorName";
    public static final String MENTOR_HOME_PHONE_NUMBER = "homePhoneNumber";
    public static final String MENTOR_CELL_PHONE_NUMBER = "cellPhoneNumber";
    public static final String MENTOR_WORK_EMAIL = "workEmailAddress";
    public static final String MENTOR_HOME_EMAIL = "homeEmailAddress";
    public static final String MENTOR_CREATED = "mentorCreated";

    //Creating array of column names
    public static final String[] ALL_MENTOR_COLUMNS =
    {
        MENTOR_ID,
        MENTOR_NAME,
        MENTOR_HOME_PHONE_NUMBER,
        MENTOR_CELL_PHONE_NUMBER,
        MENTOR_WORK_EMAIL,
        MENTOR_HOME_EMAIL,
        MENTOR_CREATED
    };

    private String mentorId;
    private String mentorName;
    private String mentorHomePhoneNumber;
    private String mentorCellPhoneNumber;
    private String mentorWorkEmail;
    private String mentorHomeEmail;
    private String mentorCreated;


    //GETTERS AND SETTERS FOR DATA MODEL - Mentor table

    public String getMentorId(){return this.mentorId;}
    public void setMentorId(String mentorId){this.mentorId = mentorId;}

    public String getMentorName(){return this.mentorName;}
    public void setMentorName(String mentorName){this.mentorName = mentorName;}

    public String getMentorHomePhoneNumber(){return this.mentorHomePhoneNumber;}
    public void setMentorHomePhoneNumber(String mentorHomePhoneNumber){this.mentorHomePhoneNumber = mentorHomePhoneNumber;}

    public String getMentorCellPhoneNumber(){return this.mentorCellPhoneNumber;}
    public void setMentorCellPhoneNumber(String mentorCellPhoneNumber){this.mentorCellPhoneNumber = mentorCellPhoneNumber;}

    public String getMentorWorkEmail(){return this.mentorWorkEmail;}
    public void setMentorWorkEmail(String mentorWorkEmail){this.mentorWorkEmail = mentorWorkEmail;}

    public String getMentorHomeEmail(){return this.mentorHomeEmail;}
    public void setMentorHomeEmail(String mentorHomeEmail){this.mentorHomeEmail = mentorHomeEmail;}

    public String getMentorCreated(){return this.mentorCreated;}
    public void setMentorCreated(String mentorCreated){this.mentorCreated = mentorCreated;}

}
