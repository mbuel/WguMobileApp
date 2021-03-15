package com.wgu.mbuel.wgumobileapp.database.List;

/**
 * Created by mbuel on 4/22/2017.
 * List object for Adapter class
 */

public class CourseList {

    private String courseId;
    private String courseTitle;
    private String courseCreated;

    public CourseList()
    {
        setCourseId("0");
        setCourseTitle("");
        setCourseCreated("");
    }

    ///GETTERS AND SETTERS FOR DATA MODEL - Course

    public void setCourseId(String courseId){this.courseId = courseId;}
    public String getCourseId(){return this.courseId;}

    public void setCourseTitle(String courseTitle){this.courseTitle = courseTitle;}
    public String getCourseTitle(){return this.courseTitle;}

    public void setCourseCreated(String courseCreated){this.courseCreated = courseCreated;}
    public String getCourseCreated(){return this.courseCreated;}

    /**
     * over rides string method to return course title. used for spinner objects
     * @return
     */
    @Override
    public String toString() {
        return this.getCourseTitle();
    }
}
