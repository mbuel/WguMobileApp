package com.wgu.mbuel.wgumobileapp.database.model;

/**
 * Created by mbuel on 4/15/2017.
 * table support class for Mentor Assignments.
 */

public class MentorAssignments {

    //INFORMATION FOR ASSIGNED_MENTORS (MENTOR ASSIGMENTS TO COURSES
    //Constants for identifying table and columns
    public static final String TABLE_MENTOR_ASSIGNMENTS = "mentorAssignments";
    public static final String MENTOR_ASSIGNMENTS_ID = "_id";
    public static final String MENTOR_ASSIGNMENTS_MENTOR_ID = "mentorAssignmentMentorId";
    public static final String MENTOR_ASSIGNMENTS_COURSE_ID = "mentorAssignmentCourseId";
    public static final String MENTOR_ASSIGNMENT_CREATED_DATE = "mentorCreated";

    //Creating array of column names
    public static final String[] ALL_MENTOR_ASSIGNMENT_COLUMNS =
    {
        MENTOR_ASSIGNMENTS_ID,
        MENTOR_ASSIGNMENTS_MENTOR_ID,
        MENTOR_ASSIGNMENTS_COURSE_ID,
        MENTOR_ASSIGNMENT_CREATED_DATE
    };

    private String mentorAsssignmentsId;
    private String mentorAssignmentsMentorId;
    private String mentorAssignmentsCourseId;
    private String mentorAssignmentCreatedDate;

    //GETTERS AND SETTERS FOR DATA MODEL - MentorAssignments
    public String getMentorAssignmentCreatedDate(){return this.mentorAssignmentCreatedDate;}
    public void setMentorAssignmentCreatedDate(String mentorAssignmentCreatedDate){this.mentorAssignmentCreatedDate = mentorAssignmentCreatedDate;}

    public String getMentorAssignmentsCourseId(){return this.mentorAssignmentsCourseId;}
    public void setMentorAssignmentsCourseId(String mentorAssignmentsCourseId){this.mentorAssignmentsCourseId = mentorAssignmentsCourseId;}

    public String getMentorAssignmentsMentorId(){return this.mentorAssignmentsMentorId;}
    public void setMentorAssignmentsMentorId(String mentorAssignmentsMentorId){this.mentorAssignmentsMentorId = mentorAssignmentsMentorId;}

    public String getMentorAssignmentsId(){return this.mentorAsssignmentsId;}
    public void setMentorAssignmentsId(String mentorAssignmentsId){this.mentorAsssignmentsId = mentorAssignmentsId;}



}
