package com.wgu.mbuel.wgumobileapp.database.model;

/**
 * Created by MBUEL on 4/15/2017.
 * Database class for AssessmentDetail data
 */

public class AssessmentDetails {
    //Constants for identifying ASSESSMENT_DETAIL table and columns

    public static final String TABLE_ASSESSMENT_DETAIL = "assessmentDetail";
    public static final String ASSESSMENT_DETAIL_ID = "_id";
    public static final String ASSESSMENT_DETAIL_TITLE = "assessmentTitle";
    public static final String ASSESSMENT_DETAIL_COURSE_DETAIL_ID = "assessmentDetailCourseId";
    public static final String ASSESSMENT_DETAIL_DUE_DATE = "dueDate";
    public static final String ASSESSMENT_DETAIL_ALARM = "goalAlarm";
    public static final String ASSESSMENT_DETAIL_COMPLETION_DATE = "assessmentCompletionDate";
    public static final String ASSESSMENT_DETAIL_TYPE = "assessmentType";
    public static final String ASSESSMENT_DETAIL_PHOTO_PATH = "assessmentPhotoPath";
    public static final String ASSESSMENT_DETAIL_NOTES = "assessmentNotes";
    public static final String ASSESSMENT_DETAIL_STATUS = "assessmentStatus";
    public static final String ASSESSMENT_DETAIL_CREATED = "assessmentCreated";

    //Creating array of ASSESSMENT_DETAIL column names
    public static final String[] ALL_ASSESSMENT_DETAIL_COLUMNS =
    {
        ASSESSMENT_DETAIL_ID,
        ASSESSMENT_DETAIL_TITLE,
        ASSESSMENT_DETAIL_COURSE_DETAIL_ID,
        ASSESSMENT_DETAIL_DUE_DATE,
        ASSESSMENT_DETAIL_ALARM,
        ASSESSMENT_DETAIL_COMPLETION_DATE,
        ASSESSMENT_DETAIL_TYPE,
        ASSESSMENT_DETAIL_PHOTO_PATH,
        ASSESSMENT_DETAIL_NOTES,
        ASSESSMENT_DETAIL_STATUS,
        ASSESSMENT_DETAIL_CREATED
    };

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

    //GETTERS AND SETTERS FOR DATA MODEL - AssessmentDetail

//    public void setAssessmentDetailId(int assessmentDetailId){this.assessmentDetailId = assessmentDetailId;}
    public String getAssessmentDetailId(){return this.assessmentDetailId;}

    public void setAssessmentDetailTitle(String assessmentDetailTitle){this.assessmentDetailTitle = assessmentDetailTitle;}
    public String getAssessmentDetailTitle(){return assessmentDetailTitle;}

    public void setAssessmentDetailCourseDetailId(String assessmentDetailCourseDetailId){this.assessmentDetailCourseDetailId = assessmentDetailCourseDetailId;}

    public String getAssessmentDetailCourseDetailId(){return assessmentDetailCourseDetailId;}

    public void setAssessmentDetailDueDate(String assessmentDetailDueDate){this.assessmentDetailDueDate = assessmentDetailDueDate;}
    public String getAssessmentDetailDueDate(){return assessmentDetailDueDate;}

    /**
     * setAssessmentDetailAlarm - programmatically controls whether the assessmentDetailAlarm is on.
     * @param assessmentDetailAlarm '0' for off / '1' for on
     */
    public void setAssessmentDetailAlarm(String assessmentDetailAlarm){this.assessmentDetailAlarm = assessmentDetailAlarm;}
    public String getAssessmentDetailAlarm(){return assessmentDetailAlarm;}

    public void setAssessmentDetailCompletionDate(String assessmentDetailCompletionDate){this.assessmentDetailCompletionDate = assessmentDetailCompletionDate;}
    public String getAssessmentDetailCompletionDate(){return assessmentDetailCompletionDate;}

    public void setAssessmentDetailType(String assessmentDetailType){this.assessmentDetailType = assessmentDetailType;}
    public String getAssessmentDetailType(){return assessmentDetailType;}

    public void setAssessmentDetailPhotoPath(String assessmentDetailPhotoPath){this.assessmentDetailPhotoPath = assessmentDetailPhotoPath;}
    public String getAssessmentDetailPhotoPath(){return assessmentDetailPhotoPath;}

    public void setAssessmentDetailNotes(String assessmentDetailNotes){this.assessmentDetailNotes = assessmentDetailNotes;}
    public String getAssessmentDetailNotes(){return assessmentDetailNotes;}

    public void setAssessmentDetailStatus(String assessmentDetailStatus){this.assessmentDetailStatus = assessmentDetailStatus;}
    public String getAssessmentDetailStatus(){return this.assessmentDetailStatus;}

    public void setAssessmentDetailCreated(String assessmentDetailCreated){this.assessmentDetailCreated = assessmentDetailCreated;}
    public String getAssessmentDetailCreated(){return this.assessmentDetailCreated;}


}
