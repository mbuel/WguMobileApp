package com.wgu.mbuel.wgumobileapp.database.List;

import com.wgu.mbuel.wgumobileapp.database.model.MentorAssignments;

/**
 * Created by mbuel on 4/23/2017.
 * List object for adapter class.
 */

public class MentorAssignmentList {
    private String mentorAsssignmentsId;
    private String mentorAssignmentsMentorId;
    private String mentorAssignmentsCourseId;
    private String mentorAssignmentCreatedDate;
    private String courseTitle;
    private String mentorName;

    public MentorAssignmentList()
    {
        setMentorAssignmentsId("0");
        setMentorAssignmentsMentorId("0");
        setMentorAssignmentsCourseId("0");
        setMentorAssignmentCreatedDate("");
        setMentorName("");
        setCourseTitle("");

    }

    //GETTERS AND SETTERS FOR DATA MODEL - MentorAssignments
    public String getMentorAssignmentCreatedDate(){return this.mentorAssignmentCreatedDate;}
    public void setMentorAssignmentCreatedDate(String mentorAssignmentCreatedDate){this.mentorAssignmentCreatedDate = mentorAssignmentCreatedDate;}

    public String getMentorAssignmentsCourseId(){return this.mentorAssignmentsCourseId;}
    public void setMentorAssignmentsCourseId(String mentorAssignmentsCourseId){this.mentorAssignmentsCourseId = mentorAssignmentsCourseId;}

    public String getMentorAssignmentsMentorId(){return this.mentorAssignmentsMentorId;}
    public void setMentorAssignmentsMentorId(String mentorAssignmentsMentorId){this.mentorAssignmentsMentorId = mentorAssignmentsMentorId;}

    public String getMentorAssignmentsId(){return this.mentorAsssignmentsId;}
    public void setMentorAssignmentsId(String mentorAssignmentsId){this.mentorAsssignmentsId = mentorAssignmentsId;}

    public String getMentorName(){return this.mentorName;}
    public void setMentorName(String mentorName){this.mentorName = mentorName;}

    public String getCourseTitle(){return this.courseTitle;}
    public void setCourseTitle(String courseTitle){this.courseTitle = courseTitle;}

}