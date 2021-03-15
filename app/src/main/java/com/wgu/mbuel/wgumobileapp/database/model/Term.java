package com.wgu.mbuel.wgumobileapp.database.model;

/**
 * Created by mbuel on 4/15/2017.
 * table support class for term data
 */

public class Term {

    //Constants for identifying table and columns
    public static final String TABLE_TERM = "term";
    public static final String TERM_ID = "_id";
    public static final String TERM_TITLE = "termTitle";
    public static final String TERM_START = "termStart";
    public static final String TERM_END = "termEnd";
    public static final String TERM_CREATED = "termCreated";

    //Creating array of column names
    public static final String[] ALL_TERM_COLUMNS = {
        TERM_ID,
        TERM_TITLE,
        TERM_START,
        TERM_END,
        TERM_CREATED
    };

    private String termId;
    private String termTitle;
    private String termStart;
    private String termEnd;
    private String termCreated;

    //GETTERS AND SETTERS FOR DATA MODEL - TERM
    public String getTermId(){return this.termId;}
    public void setTermId(String termId){this.termId = termId;}

    public String getTermTitle(){return this.termTitle;}
    public void setTermTitle(String termTitle){this.termTitle = termTitle;}

    public String getTermStart(){return this.termStart;}
    public void setTermStart(String termStart){this.termStart = termStart;}

    public String getTermEnd(){return this.termEnd;}
    public void setTermEnd(String termEnd){this.termEnd = termEnd;}

    public String getTermCreated(){return this.termCreated;}
    public void setTermCreated(String termCreated){this.termCreated = termCreated;}

}
