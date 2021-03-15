package com.wgu.mbuel.wgumobileapp.database.model;

/**
 * Created by mbuel on 4/15/2017.
 * support class for Course data
 */

public class Course {


    //Constants for identifying COURSE table and columns

    public static final String TABLE_COURSE = "course";
    public static final String COURSE_ID = "_id";
    public static final String COURSE_TITLE = "courseTitle";
    public static final String COURSE_CREATED = "courseCreated";

    //Creating array of column names
    public static final String[] ALL_COURSE_COLUMNS =
    {
        COURSE_ID,
        COURSE_TITLE,
        COURSE_CREATED
    };

    private String courseId;
    private String courseTitle;
    private String courseCreated;

    ///GETTERS AND SETTERS FOR DATA MODEL - Course

    public void setCourseId(String courseId){this.courseId = courseId;}
    public String getCourseId(){return this.courseId;}

    public void setCourseTitle(String courseTitle){this.courseTitle = courseTitle;}
    public String getCourseTitle(){return this.courseTitle;}

    public void setCourseCreated(String courseCreated){this.courseCreated = courseCreated;}
    public String getCourseCreated(){return this.courseCreated;}

}
