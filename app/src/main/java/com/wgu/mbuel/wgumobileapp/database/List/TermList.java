package com.wgu.mbuel.wgumobileapp.database.List;

import com.wgu.mbuel.wgumobileapp.database.model.Term;

/**
 * Created by mbuel on 4/21/2017.
 * List object for adapter class.
 */

public class TermList {

    private Term term;

    private String termId;
    private String termTitle;
    private String termStart;
    private String termEnd;
    private String termCreated;

    public TermList()
    {
        term = new Term();
        setTermTitle("");
        setTermEnd("");
        setTermStart("");
        setTermId("0");
        setTermCreated("");
    }

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

    public Term getTerm(){return this.term;}
}
