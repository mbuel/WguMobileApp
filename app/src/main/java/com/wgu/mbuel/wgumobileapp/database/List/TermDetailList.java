package com.wgu.mbuel.wgumobileapp.database.List;

/**
 * Created by mbuel on 4/22/2017.
 * List object for adapter class.
 */


import android.util.Log;

/**
 * TermDetailList object - uses the CourseDetails data and model classes as a template. It fills the TermCourseDetail Activity.
 */
public class TermDetailList {

    private String courseDetailsId;
    private String courseDetailsCourseId;
    private String courseDetailsTermId;
    private String courseDetailsNotes;
    private String courseTitle;
    private String courseDetailsStartDate;
    private String courseDetailsStartAlarm;
    private String courseDetailsEndDate;
    private String courseDetailsEndAlarm;
    private String courseDetailsStatus;
    private String termTitle;

    public TermDetailList() {
        setCourseDetailsId("0");
        setCourseDetailsTermId("0");
        setCourseDetailsCourseId("0");
        setCourseDetailsNotes("");
        setCourseTitle("");
        setCourseDetailsStartDate("");
        setCourseDetailsStartAlarm("");
        setCourseDetailsEndDate("");
        setCourseDetailsEndDate("");
        setCourseDetailsStatus("");
        setTermTitle("");

    }

    //GETTERS AND SETTERS FOR DATA MODEL - CourseDetails
    public String getCourseDetailsId() {
        return this.courseDetailsId;
    }

    public void setCourseDetailsId(String courseDetailsId) {
        this.courseDetailsId = courseDetailsId;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDetailsCourseId() {
        return this.courseDetailsCourseId;
    }

    public void setCourseDetailsCourseId(String courseDetailsCourseId) {
        this.courseDetailsCourseId = courseDetailsCourseId;
    }

    public String getCourseDetailsTermId() {
        Log.d("TermDetailList", "RETURNED TERM ID: " + this.courseDetailsTermId);
        return this.courseDetailsTermId;
    }

    public void setCourseDetailsTermId(String courseDetailsTermId) {
        Log.d("TermDetailList", "TERM ID: " + courseDetailsTermId);
        this.courseDetailsTermId = courseDetailsTermId;
    }

    public String getCourseDetailsNotes() {
        return this.courseDetailsNotes;
    }

    public void setCourseDetailsNotes(String courseDetailsNotes) {
        this.courseDetailsNotes = courseDetailsNotes;
    }

    public String getCourseDetailsStartDate() {
        return this.courseDetailsStartDate;
    }

    public void setCourseDetailsStartDate(String courseDetailsStartDate) {
        this.courseDetailsStartDate = courseDetailsStartDate;
    }

    public String getCourseDetailsStartAlarm() {
        return this.courseDetailsStartAlarm;
    }

    public void setCourseDetailsStartAlarm(String courseDetailsStartAlarm) {
        this.courseDetailsStartAlarm = courseDetailsStartAlarm;
    }

    public String getCourseDetailsEndDate() {
        return this.courseDetailsEndDate;
    }

    public void setCourseDetailsEndDate(String courseDetailsEndDate) {
        this.courseDetailsEndDate = courseDetailsEndDate;
    }

    public String getCourseDetailsEndAlarm() {
        return this.courseDetailsEndAlarm;
    }

    public void setCourseDetailsEndAlarm(String courseDetailsEndAlarm) {
        this.courseDetailsEndAlarm = courseDetailsEndAlarm;
    }

    public String getCourseDetailsStatus() {
        return this.courseDetailsStatus;
    }

    public void setCourseDetailsStatus(String courseDetailsStatus) {
        this.courseDetailsStatus = courseDetailsStatus;
    }

    public String getTermTitle(){return this.termTitle;}

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }
}

