//DONE: FINISH THIS CLASS
//DONE: 1) Comment all methods
//DONE: 2) Add delete button to action bar via code. (need to Google)
//DONE: 3) Make sure delete button has code to check for assigned Courses to that term. (CourseDetail arrayList object can accomplish)

package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.database.List.CourseList;
import com.wgu.mbuel.wgumobileapp.database.List.TermDetailList;
import com.wgu.mbuel.wgumobileapp.database.List.TermList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.TermListAdapter;
import com.wgu.mbuel.wgumobileapp.database.model.Term;
import com.wgu.mbuel.wgumobileapp.database.repo.CourseDetailsRepo;
import com.wgu.mbuel.wgumobileapp.database.repo.TermRepo;

import java.util.ArrayList;

public class TermView
        extends AppCompatActivity
        implements View.OnClickListener, ListView.OnItemClickListener, ListView.OnItemLongClickListener {

    public static final int REQUEST_CODE = 1002;
    private final String CLASS_NAME = this.getClass().getName();

    private CommonFunctions commonFunctions = new CommonFunctions();
    //Array of TermList objects
    private ArrayList<TermList> termLists;

    //List Adapter
    private TermListAdapter termListAdapter;

    //Database connection
    private TermRepo termRepo = new TermRepo();

    private Intent setIntent;

    private ListView termListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabTermList = (FloatingActionButton) findViewById(R.id.fabTermView);
        fabTermList.setOnClickListener(this);

        refreshListView();
        setTitle("Term List");

        Log.d(CLASS_NAME,"Size of termLists: " + termLists.size());



        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException npE)
        {
            Log.e(CLASS_NAME,"Invocation problem with up button." + "\n" + npE.getMessage(),npE);
        }

    }

    /**
     * refresh ListView after delete or return from Course Assignment
     */
    private void refreshListView()
    {
        termLists = termRepo.readTermList();

        termListAdapter = new TermListAdapter(App.getContext(),0,termLists);
        termListAdapter.notifyDataSetChanged();

        termListView = (ListView) findViewById(R.id.listTerms);
        termListView.setAdapter(termListAdapter);
        termListView.setOnItemClickListener(this);
        termListView.setOnItemLongClickListener(this);

    }
    @Override
    public void onClick(View v)
    {
        Log.d(CLASS_NAME,"In onClick. View: " + v);
        Log.d(CLASS_NAME,"R.id.fabTermView: " + R.id.fabTermView);
        if (v == findViewById(R.id.fabTermView)){
            Log.d(CLASS_NAME,"Clicked make term.");
            this.setIntent = new Intent(TermView.this,TermSelection.class);
            TermList termList = new TermList();

            setIntentLaunchActivity(termList);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(CLASS_NAME,"View value: " + view);
        Log.d(CLASS_NAME,"R.id.listTerms: " + R.id.listTerms);
        Log.d(CLASS_NAME,"Selected position: " + position);
        Log.d(CLASS_NAME,"Selected id: " + id);

        TermList selectedTerm = (TermList) this.termListView.getItemAtPosition(position);

        this.setIntent = new Intent(TermView.this,TermSelection.class);

        setIntentLaunchActivity(selectedTerm);
    }
    /**
     * onItemCLick - load details of selected item, then pass to next activity
     * @param parent this activity
     * @param view the list view containing this item
     * @param position position of selected item
     * @param id id of selected item
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int deletedRowId = 0;
        int termId = 0;
        TermList deleteTerm = (TermList) this.termListView.getItemAtPosition(position);
        Term termToDelete = new Term();
        termToDelete.setTermId(deleteTerm.getTermId());
        termId = this.commonFunctions.tryParseInt(deleteTerm.getTermId())?commonFunctions.getInteger():0;
        //TermDetailList readList = new TermDetailList();
        CourseDetailsRepo cdRepo = new CourseDetailsRepo();
        ArrayList<TermDetailList> deleteTermList = cdRepo.readTermDetailArrayList(termId);
        //readList.setCourseDetailsTermId(deleteTerm.getTermId());
        Log.d(CLASS_NAME,"termDetailLists empty? " + deleteTermList.isEmpty());
        if (deleteTermList.isEmpty()){ //Validation to make sure no courses are assigned - REQ A.3
            termRepo.delete(termToDelete);
            commonFunctions.makeToast("Term Deleted. ");
            refreshListView();
            //finish();
            return true;
        }
        else
        {
            commonFunctions.makeToast("Unable to delete term with courses assigned. ");
            return false;
        }

        //return false;
    }
    /**
     * setIntentLaunchActivity
     * @param termList term object to populate for next activity
     */
    private void setIntentLaunchActivity(TermList termList)
    {
        setIntent.putExtra(Term.TERM_ID, termList.getTermId());
        setIntent.putExtra(Term.TERM_TITLE,termList.getTermTitle());
        setIntent.putExtra(Term.TERM_START,termList.getTermStart());
        setIntent.putExtra(Term.TERM_END,termList.getTermEnd());
        setIntent.putExtra(Term.TERM_CREATED,termList.getTermCreated());

        startActivityForResult(this.setIntent,REQUEST_CODE);
    }

    /**
     * onActivityResult - populate ListView
     * @param requestCode - code from caller
     * @param resultCode result code from caller
     * @param data bundled data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        refreshListView();
    }


}
