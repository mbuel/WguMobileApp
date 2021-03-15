//DONE: (THIS WEEKEND) start Landscape Layouts. (Notepad example)
package com.wgu.mbuel.wgumobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CODE= 1001;
    private final String CLASS_NAME = this.getClass().getName();

    /**
     * onCreate -
     * @param savedInstanceState bundle of saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("WGU MOBILE APPLICATIONS");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnTermList = (Button) findViewById(R.id.btnTermView);
        btnTermList.setOnClickListener(this);

        Button btnMentorList = (Button) findViewById(R.id.btnMentorEdit);
        btnMentorList.setOnClickListener(this);

        Button btnCourseList = (Button) findViewById(R.id.btnCourseEdit);
        btnCourseList.setOnClickListener(this);

    }

    /**
     *  onClick for buttons on Activity
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        if (view == findViewById(R.id.btnTermView)){
            try {


                intent = new Intent(MainActivity.this, TermView.class);
//                Uri uri = Uri.parse(TermProvider.CONTENT_URI + "/" + id);
//                intent.putExtra(TermProvider.TERM_CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, REQUEST_CODE);
            }
            catch(Exception e)
            {
                Log.e(CLASS_NAME, "ERROR LAUNCHING TERM VIEW." + "\n" + e.getMessage() );
            }
            //load next view
        }
        else if(view == findViewById(R.id.btnMentorEdit))
        {
            Log.d(CLASS_NAME,"Trying to launch Mentor List.");
            intent = new Intent(MainActivity.this, MentorView.class);
            //Uri uri = Uri.parse(TermProvider.CONTENT_URI + "/" + id);
            //intent.putExtra(TermProvider.TERM_CONTENT_ITEM_TYPE, uri);

            startActivityForResult(intent, REQUEST_CODE);
        }
        else if (view == findViewById(R.id.btnCourseEdit))
        {
            Log.d(CLASS_NAME,"Trying to launch Course List.");
            intent = new Intent(MainActivity.this, CourseView.class);
            //Uri uri = Uri.parse(TermProvider.CONTENT_URI + "/" + id);
            //intent.putExtra(TermProvider.TERM_CONTENT_ITEM_TYPE, uri);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }
}
