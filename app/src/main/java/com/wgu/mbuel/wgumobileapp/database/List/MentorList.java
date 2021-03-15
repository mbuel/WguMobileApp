package com.wgu.mbuel.wgumobileapp.database.List;

/**
 * Created by mbuel on 4/23/2017.
 * List object for adapter class.
 */

public class MentorList {

    private String mentorId;
    private String mentorName;
    private String mentorHomePhoneNumber;
    private String mentorCellPhoneNumber;
    private String mentorWorkEmail;
    private String mentorHomeEmail;
    private String mentorCreated;

    public MentorList() {
        setMentorId("0");
        setMentorName("");
        setMentorHomeEmail("");
        setMentorCellPhoneNumber("");
        setMentorHomePhoneNumber("");
        setMentorWorkEmail("");
        setMentorCreated("");
    }
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
    @Override
    public String toString() {
        return getMentorName();
    }
}


