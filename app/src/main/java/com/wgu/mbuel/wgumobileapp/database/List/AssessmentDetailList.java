package com.wgu.mbuel.wgumobileapp.database.List;

/**
 * Created by mbuel on 4/18/2017.
 * List object for adapter class
 */

public class AssessmentDetailList {

    private String assessmentDetailId;
    private String assessmentDetailTitle; //title of assessment
    private String assessmentDetailCourseDetailId; //relationship to CourseDetail table
    private String assessmentDetailDueDate; //due date of assessment
    private String assessmentDetailAlarm; //enable assessmentDetailAlarm for due date
    private String assessmentDetailCompletionDate; //date assessment completed
    private String assessmentDetailType;
    private String assessmentDetailPhotoPath;
    private String assessmentDetailNotes;
    private String assessmentDetailStatus;
    private String assessmentDetailCreated;

    public AssessmentDetailList()
    {
        setAssessmentDetailId("0");
        setAssessmentDetailTitle("");
        setAssessmentDetailDueDate("");
        setAssessmentDetailCourseDetailId("0");
        setAssessmentDetailAlarm("");
        setAssessmentDetailCompletionDate("");
        setAssessmentDetailType("");
        setAssessmentDetailPhotoPath("");
        setAssessmentDetailNotes("");
        setAssessmentDetailStatus("");
        setAssessmentDetailCreated("");
    }

    public void setAssessmentDetailId(String assessmentDetailId){this.assessmentDetailId = assessmentDetailId;}
    public String getAssessmentDetailId(){return this.assessmentDetailId;}

    public void setAssessmentDetailTitle(String assessmentDetailTitle){this.assessmentDetailTitle = assessmentDetailTitle;}
    public String getAssessmentDetailTitle(){return assessmentDetailTitle;}

    public void setAssessmentDetailCourseDetailId(String assessmentDetailCourseDetailId){this.assessmentDetailCourseDetailId = assessmentDetailCourseDetailId;}
    public String getAssessmentDetailCourseDetailId(){return this.assessmentDetailCourseDetailId;}

    public void setAssessmentDetailDueDate(String assessmentDetailDueDate){this.assessmentDetailDueDate = assessmentDetailDueDate;}
    public String getAssessmentDetailDueDate(){return this.assessmentDetailDueDate;}

    /**
     * setAssessmentDetailAlarm - programmatically controls whether the assessmentDetailAlarm is on.
     * @param assessmentDetailAlarm '0' for off / '1' for on
     */
    public void setAssessmentDetailAlarm(String assessmentDetailAlarm){this.assessmentDetailAlarm = assessmentDetailAlarm;}
    public String getAssessmentDetailAlarm(){return this.assessmentDetailAlarm;}

    public void setAssessmentDetailCompletionDate(String assessmentDetailCompletionDate){this.assessmentDetailCompletionDate = assessmentDetailCompletionDate;}
    public String getAssessmentDetailCompletionDate(){return this.assessmentDetailCompletionDate;}

    public void setAssessmentDetailType(String assessmentDetailType){this.assessmentDetailType = assessmentDetailType;}
    public String getAssessmentDetailType(){return this.assessmentDetailType;}

    public void setAssessmentDetailPhotoPath(String assessmentDetailPhotoPath){this.assessmentDetailPhotoPath = assessmentDetailPhotoPath;}
    public String getAssessmentDetailPhotoPath(){return this.assessmentDetailPhotoPath;}

    public void setAssessmentDetailNotes(String assessmentDetailNotes){this.assessmentDetailNotes = assessmentDetailNotes;}
    public String getAssessmentDetailNotes(){return this.assessmentDetailNotes;}

    public void setAssessmentDetailStatus(String assessmentDetailStatus){this.assessmentDetailStatus = assessmentDetailStatus;}
    public String getAssessmentDetailStatus(){return this.assessmentDetailStatus;}

    public void setAssessmentDetailCreated(String assessmentDetailCreated){this.assessmentDetailCreated = assessmentDetailCreated;}
    public String getAssessmentDetailCreated(){return this.assessmentDetailCreated;}

    @Override
    public String toString() {
        return getAssessmentDetailTitle();
    }
}
