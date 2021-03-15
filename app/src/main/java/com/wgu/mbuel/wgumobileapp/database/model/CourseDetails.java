package com.wgu.mbuel.wgumobileapp.database.model;

/**
 * Created by mbuel on 4/15/2017.
 * support class for CourseDetails table and TermCourseDetail Activity
 */

public class CourseDetails {

    //Constants for identifying COURSE_DETAILS table and columns
    public static final String TABLE_COURSE_DETAILS = "courseDetails";
    public static final String COURSE_DETAILS_ID = "_id";
    public static final String COURSE_DETAILS_COURSE_ID = "courseId";
    public static final String COURSE_DETAILS_TERM_ID = "termId";
    public static final String COURSE_DETAILS_NOTES = "notes";
    public static final String COURSE_DETAILS_START_DATE = "startDate";
    public static final String COURSE_DETAILS_START_ALARM = "startAlarmEnabled";
    public static final String COURSE_DETAILS_END_DATE = "endDate";
    public static final String COURSE_DETAILS_END_ALARM = "endAlarmEnabled";
    public static final String COURSE_DETAILS_STATUS = "courseDetailStatus";
    public static final String COURSE_DETAILS_CREATED = "courseDetailsCreated";

    public static final String TERM_TITLE = "termTitle";
    public static final String COURSE_TITLE = "courseTitle";

    //Creating array of column names
    public static final String[] ALL_COURSE_DETAILS_COLUMNS =
    {
        COURSE_DETAILS_ID,
        COURSE_DETAILS_COURSE_ID,
        COURSE_DETAILS_TERM_ID,
        COURSE_DETAILS_NOTES,
        COURSE_DETAILS_START_DATE,
        COURSE_DETAILS_START_ALARM,
        COURSE_DETAILS_END_DATE,
        COURSE_DETAILS_END_ALARM,
        COURSE_DETAILS_STATUS,
        COURSE_DETAILS_CREATED
    };

    private String courseDetailsId;
    private String courseDetailsCourseId;
    private String courseDetailsTermId;
    private String courseDetailsNotes;
    private String courseDetailsStartDate;
    private String courseDetailsStartAlarm;
    private String courseDetailsEndDate;
    private String courseDetailsEndAlarm;
    private String courseDetailsStatus;


    //GETTERS AND SETTERS FOR DATA MODEL - CourseDetails
    public String getCourseDetailsId() {return this.courseDetailsId;}
    public void setCourseDetailsId(String courseDetailsId){this.courseDetailsId = courseDetailsId;}

    public String getCourseDetailsCourseId() {return this.courseDetailsCourseId;}
    public void setCourseDetailsCourseId(String courseDetailsCourseId) {this.courseDetailsCourseId = courseDetailsCourseId;}

    public String getCourseDetailsTermId(){return this.courseDetailsTermId;}
    public void setCourseDetailsTermId(String courseDetailsTermId){this.courseDetailsTermId = courseDetailsTermId;}

    public String getCourseDetailsNotes(){return this.courseDetailsNotes;}
    public void setCourseDetailsNotes(String courseDetailsNotes){this.courseDetailsNotes = courseDetailsNotes;}

    public String getCourseDetailsStartDate(){return this.courseDetailsStartDate;}
    public void setCourseDetailsStartDate(String courseDetailsStartDate){this.courseDetailsStartDate = courseDetailsStartDate;}

    public String getCourseDetailsStartAlarm(){return this.courseDetailsStartAlarm;}
    public void setCourseDetailsStartAlarm(String courseDetailsStartAlarm){this.courseDetailsStartAlarm = courseDetailsStartAlarm;}

    public String getCourseDetailsEndDate(){return this.courseDetailsEndDate;}
    public void setCourseDetailsEndDate(String courseDetailsEndDate){this.courseDetailsEndDate = courseDetailsEndDate;}

    public String getCourseDetailsEndAlarm(){return this.courseDetailsEndAlarm;}
    public void setCourseDetailsEndAlarm(String courseDetailsEndAlarm){this.courseDetailsEndAlarm = courseDetailsEndAlarm;}

    public String getCourseDetailsStatus(){return this.courseDetailsStatus;}
    public void setCourseDetailsStatus(String courseDetailsStatus){this.courseDetailsStatus = courseDetailsStatus;}


}
