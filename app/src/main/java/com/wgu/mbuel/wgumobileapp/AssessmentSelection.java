package com.wgu.mbuel.wgumobileapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.wgu.mbuel.wgumobileapp.app.App;
import com.wgu.mbuel.wgumobileapp.app.SoftKeyboardLsnedRelativeLayout;
import com.wgu.mbuel.wgumobileapp.database.List.AssessmentDetailList;
import com.wgu.mbuel.wgumobileapp.database.adapter.CommonFunctions;
import com.wgu.mbuel.wgumobileapp.database.adapter.ImageProcessor;
import com.wgu.mbuel.wgumobileapp.database.model.AssessmentDetails;
import com.wgu.mbuel.wgumobileapp.database.repo.AssessmentDetailRepo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentSelection
        extends AppCompatActivity
        implements View.OnClickListener {
    public static final int REQUEST_CODE= 1010;
    private static final int MENU_ITEM_SAVE=1;
    private static final int MENU_ITEM_DELETE=2;
    private static final String ASSESSMENT_SELECTION_NAME = "assessmentSelection";
    private final String CLASS_NAME = this.getClass().getName();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Date currentDate =  new Date();
    private String currentDay = this.sdf.format(this.currentDate);

    private int selectedAssessmentId;

    private Intent getIntent;
    private Bundle getBundle;

    private CommonFunctions commonFunctions = new CommonFunctions();
    private ImageProcessor imageProcessor = new ImageProcessor(App.getContext());
    private String imagePath ="";
    private AssessmentDetailRepo adRepo = new AssessmentDetailRepo();
    private AssessmentDetailList assessmentDetailList;

    private EditText assessmentTitle, assessmentType, assessmentDue, assessmentCompletion, assessmentStatus, assessmentNotes;
    private ImageView assessmentPhoto;
    private CheckBox assessmentAlarm;

    /**
     * AssessmentSelection onCreate - create and instantiate the Activity for Assessment Details
     * @param savedInstanceState - bundled values from prior activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        initActivity();
        this.getIntent = getIntent();

        if (this.getIntent != null) {
            this.assessmentDetailList = new AssessmentDetailList();
            this.getBundle = this.getIntent.getExtras();
            this.assessmentDetailList.setAssessmentDetailCourseDetailId(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID));
            Log.d(CLASS_NAME, "Course Details ID: " + this.assessmentDetailList.getAssessmentDetailCourseDetailId());

        }
        else if (this.assessmentDetailList == null)
        {
            restorePrefs();
        }

        this.selectedAssessmentId = commonFunctions.tryParseInt(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_ID)) ? commonFunctions.getInteger() : 0;

        if (this.selectedAssessmentId > 0) {
            this.assessmentDetailList.setAssessmentDetailId(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_ID));
            setActivityValues();
            setTitle(this.assessmentDetailList.getAssessmentDetailTitle() + " VIEW");
        }
        else
        {
            setTitle("NEW ASSESSMENT");
            this.assessmentDue.setText(this.currentDay);
        }

        final FloatingActionButton fabTakePicture = (FloatingActionButton) findViewById(R.id.fabAssessmentList);
        fabTakePicture.setOnClickListener(this);

        SoftKeyboardLsnedRelativeLayout layout = (SoftKeyboardLsnedRelativeLayout) findViewById(R.id.activityAssessmentDetail);
        Log.d(CLASS_NAME,"Layout is null? " + (layout == null));
        Log.d(CLASS_NAME,"R.id.activityAssessmentDetail is null? " + (R.id.activityAssessmentDetail == 0));
        layout.addSoftKeyboardLsner(new SoftKeyboardLsnedRelativeLayout.SoftKeyboardLsner() {
            @Override
            public void onSoftKeyboardShow() {
                Log.d(CLASS_NAME,"Soft Keyboard shown");
                assessmentPhoto.setVisibility(View.GONE);
                fabTakePicture.setVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardHide() {
                Log.d(CLASS_NAME,"Soft Keyboard hidden.");
                assessmentPhoto.setVisibility(View.VISIBLE);
                fabTakePicture.setVisibility(View.VISIBLE);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * setActivityValues - after the objects are initialized they are populated with the values
     */
    private void setActivityValues() {
        this.assessmentTitle.setText(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_TITLE));
        this.assessmentDetailList.setAssessmentDetailTitle(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_TITLE));

        this.assessmentType.setText(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_TYPE));
        this.assessmentDetailList.setAssessmentDetailType(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_TYPE));

        this.assessmentDue.setText(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE));
        this.assessmentDetailList.setAssessmentDetailDueDate(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE));

        this.assessmentNotes.setText(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_NOTES));
        this.assessmentDetailList.setAssessmentDetailNotes(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_NOTES));

        this.assessmentCompletion.setText(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE));
        this.assessmentDetailList.setAssessmentDetailCompletionDate(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE));

        this.assessmentStatus.setText(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_STATUS));
        this.assessmentDetailList.setAssessmentDetailStatus(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_STATUS));

        this.assessmentDetailList.setAssessmentDetailPhotoPath(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH));
        Log.d(CLASS_NAME,"Assessment Selection Photo Path: " + this.assessmentDetailList.getAssessmentDetailPhotoPath());
        this.imagePath = this.assessmentDetailList.getAssessmentDetailPhotoPath();
        Log.d(CLASS_NAME,"this.imagePath: " + this.imagePath);
        File checkImageStatus = new File(this.imagePath);
        Log.d(CLASS_NAME,"Image exists? " + checkImageStatus.exists());
        this.assessmentPhoto.setImageBitmap(this.imageProcessor.getImageBitmap(App.getContext(),this.imagePath));

        this.assessmentDetailList.setAssessmentDetailAlarm(this.getBundle.getString(AssessmentDetails.ASSESSMENT_DETAIL_ALARM));
        if (commonFunctions.tryParseInt(this.assessmentDetailList.getAssessmentDetailAlarm())) {
            this.assessmentAlarm.setChecked(commonFunctions.getInteger() > 0);
            if (this.assessmentAlarm.isChecked())
            {
                displayAlert(this.assessmentDetailList.getAssessmentDetailDueDate(),"ASSESSMENT DUE!");
            }
        }


    }

    /**
     * displayAlert - displays alert if alarm is checked and the date has arrived.
     * @param date - date to check against today
     * @param callback - callback string to display in Alert
     */
    private void displayAlert(String date, String callback)
    {
        Log.d(CLASS_NAME,"Current Day String: " + this.currentDay);
        if (this.currentDay.equals(date)) {
            new AlertDialog.Builder(AssessmentSelection.this)
                .setMessage(callback + " day is here!")
                .setCancelable(true).create().show();
        }
    }

    /**
     * initActivity - initializes all the objects on the Activity
     */
    private void initActivity() {

        this.assessmentTitle = (EditText) findViewById(R.id.editTextAssessmentTitle);
        this.assessmentType = (EditText) findViewById(R.id.editTextAssessmentType);
        this.assessmentDue = (EditText) findViewById(R.id.editTextAssessmentDueDate);
        this.assessmentCompletion = (EditText) findViewById(R.id.editTextAssessmentCompletionDate);
        this.assessmentStatus = (EditText) findViewById(R.id.editTextAssessmentStatus);
        this.assessmentNotes = (EditText) findViewById(R.id.editTextAssessmentNotes);
        this.assessmentPhoto = (ImageView) findViewById(R.id.imageViewAssessmentCameraCapture);
        this.assessmentPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
        this.assessmentAlarm = (CheckBox) findViewById(R.id.checkBoxAssessmentAlarm);

    }

    /**
     * Activate the camera
     * @param v
     */
    @Override
    public void onClick(View v) {
        updateAssessmentDetails();
        saveAssessmentDetails();
        //ImageProcessor takePhoto = new ImageProcessor(App.getContext(),this.assessmentDetailList.getAssessmentDetailId()).toString().substring();
        try {
            File testFile = new File("/Android/data/" + AssessmentSelection.this.getPackageName() + "/");
            Log.d(CLASS_NAME,"ABSOLUTE FILE PATH: " + testFile.getParentFile().getParentFile().getParentFile());
            Log.d(CLASS_NAME,"Environment root: " + Environment.getRootDirectory());
            Log.d(CLASS_NAME,"Environment Path: " + Environment.getExternalStorageDirectory());
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED))
            {
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                Log.d(CLASS_NAME,"Environment Path: " + Environment.getRootDirectory() + "/" + "\n" + Environment.getExternalStorageDirectory() + "\n" + Environment.getExternalStorageDirectory());

                String localPhotoPath = Environment.getExternalStorageDirectory() + "/Android/data/"  +
                        AssessmentSelection.this.getPackageName() + "/" + this.assessmentDetailList.getAssessmentDetailTitle().replace(" ","") +
                        this.assessmentDetailList.getAssessmentDetailId() + ".jpg";
                localPhotoPath = localPhotoPath.substring(localPhotoPath.indexOf("/")+1);
                Log.d(CLASS_NAME,"local path: " + localPhotoPath);
                File localPhotoFile = new File(localPhotoPath);
                try
                {
                    if(!localPhotoFile.exists()){
                        if (localPhotoFile.getParentFile().mkdirs()) {
                            localPhotoFile.createNewFile();
                        }
                    }
                }
                catch (IOException e)
                {
                    Log.e(CLASS_NAME,"could not create file." , e);
                }

                Uri photoFileUri = Uri.fromFile(localPhotoFile);
                Log.d(CLASS_NAME,"Content Path: " + photoFileUri);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                this.imagePath = photoFileUri.toString();
                Log.d(CLASS_NAME,"Stored Image Path: " + this.imagePath);
                this.assessmentDetailList.setAssessmentDetailPhotoPath(this.imagePath);
                saveAssessmentDetails();
                writePrefs();
                startActivityForResult(cameraIntent, REQUEST_CODE);

            }
            else
            {
                new AlertDialog.Builder(AssessmentSelection.this)
                    .setMessage("Cannot locate storage to save pictures. Are you able to save pictures in the camera app outside of this app?")
                    .setCancelable(true).create().show();
            }


        }

        catch(Exception e)
        {
            Log.d(CLASS_NAME,"PROBLEM TAKING PICTURE. " + "\n" + e.getMessage() + "\n" + e.getStackTrace().toString());
        }

    }
    /** onCreateOptionsMenu - creates menu via code, filling requirement _____
     *
     * @param menu
     * @return always returns true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_SAVE, Menu.NONE, "save");
        menu.add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "delete");
        return true;
    }
    /**
     * onOptionsItemSelected - select Save/Delete and process data
     * @param item
     * @return true/false if action was successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ITEM_SAVE:


                return saveAssessmentDetails() > 0;
            case MENU_ITEM_DELETE:

                adRepo.delete(this.assessmentDetailList);
                finish();
                return  true;

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;

            //DONE: need to close form after delete.
        }

    }

    /**
     * updateAssessmentDetails - updates data object from activity values
     */
    private void updateAssessmentDetails() {
        this.assessmentDetailList.setAssessmentDetailTitle(this.assessmentTitle.getText().toString());
        this.assessmentDetailList.setAssessmentDetailDueDate(this.assessmentDue.getText().toString());
        this.assessmentDetailList.setAssessmentDetailStatus(this.assessmentStatus.getText().toString());
        this.assessmentDetailList.setAssessmentDetailType(this.assessmentType.getText().toString());
        this.assessmentDetailList.setAssessmentDetailAlarm((this.assessmentAlarm.isChecked()?"1":"0"));
        this.assessmentDetailList.setAssessmentDetailCompletionDate(this.assessmentCompletion.getText().toString());
        this.assessmentDetailList.setAssessmentDetailNotes(this.assessmentNotes.getText().toString());
    }

    /**
     * saveAssessmentDetails - saves the data from this activity to the database.
     * @return true/false if save successful
     */
    private int saveAssessmentDetails() {
        updateAssessmentDetails();
        int assessmentId = commonFunctions.tryParseInt(this.assessmentDetailList.getAssessmentDetailId())?this.commonFunctions.getInteger():0;
        int saveResults = 0;
        if (!(this.assessmentTitle.getText().toString().isEmpty())){
            if (assessmentId > 0){
                saveResults = adRepo.update(this.assessmentDetailList);

            }
            else
            {
                //Make sure course doesn't already exist in the database.
                saveResults = this.adRepo.create(this.assessmentDetailList);
                this.assessmentDetailList.setAssessmentDetailId(String.valueOf(saveResults));
                setTitle(this.assessmentDetailList.getAssessmentDetailTitle() + " VIEW");
            }

        }
        else
        {
            Log.d(CLASS_NAME,"TITLE IS EMPTY.");
        }
        return saveResults;
    }

    /**
     * writePrefs - write sharedPrefs to local storage
     */
    private void writePrefs()
    {
        updateAssessmentDetails();
        SharedPreferences.Editor setAssessDetailPref = getSharedPreferences(ASSESSMENT_SELECTION_NAME, MODE_PRIVATE).edit();

        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_ID,this.assessmentDetailList.getAssessmentDetailId());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_ALARM,this.assessmentDetailList.getAssessmentDetailAlarm());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH,this.imagePath);
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_STATUS,this.assessmentDetailList.getAssessmentDetailStatus());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE,this.assessmentDetailList.getAssessmentDetailCompletionDate());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID,this.assessmentDetailList.getAssessmentDetailCourseDetailId());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_CREATED,this.assessmentDetailList.getAssessmentDetailCreated());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE,this.assessmentDetailList.getAssessmentDetailDueDate());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_NOTES,this.assessmentDetailList.getAssessmentDetailNotes());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_TITLE,this.assessmentDetailList.getAssessmentDetailTitle());
        setAssessDetailPref.putString(AssessmentDetails.ASSESSMENT_DETAIL_TYPE,this.assessmentDetailList.getAssessmentDetailType());


        setAssessDetailPref.apply();
    }

    /**
     * restorePrefs - Restore the shared Prefs from the local storage
     */
    private void restorePrefs() {
        SharedPreferences getAssessDetailPref = getSharedPreferences(ASSESSMENT_SELECTION_NAME, MODE_PRIVATE);
        if (this.assessmentDetailList == null)
        {
            this.assessmentDetailList = new AssessmentDetailList();
        }

        this.assessmentDetailList.setAssessmentDetailId(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_ID,this.assessmentDetailList.getAssessmentDetailId()));
        this.assessmentDetailList.setAssessmentDetailAlarm(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_ALARM,this.assessmentDetailList.getAssessmentDetailAlarm()));
        this.assessmentDetailList.setAssessmentDetailPhotoPath(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_PHOTO_PATH,this.assessmentDetailList.getAssessmentDetailPhotoPath()));
        this.imagePath = this.assessmentDetailList.getAssessmentDetailPhotoPath();
        this.assessmentDetailList.setAssessmentDetailStatus(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_STATUS,this.assessmentDetailList.getAssessmentDetailStatus()));
        this.assessmentDetailList.setAssessmentDetailCompletionDate(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_COMPLETION_DATE,this.assessmentDetailList.getAssessmentDetailCompletionDate()));
        this.assessmentDetailList.setAssessmentDetailCourseDetailId(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_COURSE_DETAIL_ID,this.assessmentDetailList.getAssessmentDetailCourseDetailId()));
        this.assessmentDetailList.setAssessmentDetailCreated(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_CREATED,this.assessmentDetailList.getAssessmentDetailCreated()));
        this.assessmentDetailList.setAssessmentDetailDueDate(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_DUE_DATE,this.assessmentDetailList.getAssessmentDetailDueDate()));
        this.assessmentDetailList.setAssessmentDetailNotes(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_NOTES,this.assessmentDetailList.getAssessmentDetailNotes()));
        this.assessmentDetailList.setAssessmentDetailTitle(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_TITLE,this.assessmentDetailList.getAssessmentDetailTitle()));
        this.assessmentDetailList.setAssessmentDetailType(getAssessDetailPref.getString(AssessmentDetails.ASSESSMENT_DETAIL_TYPE,this.assessmentDetailList.getAssessmentDetailType()));

    }

    /**
     * onActivityResult - return results from Camera, load into form
     * @param requestCode - code of passed Activity
     * @param resultCode - resultCode of Activity
     * @param data - data bundle from activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AssessmentSelection.REQUEST_CODE && resultCode == RESULT_OK)
        {
            initActivity();
            restorePrefs();
            Intent intent = getIntent();
            Bundle getPhotoPath = intent.getExtras();
            Log.d(CLASS_NAME,"getPhotoPath is null? " + (getPhotoPath == null));
            Log.d(CLASS_NAME,"Photo Path: " + getPhotoPath.getString(MediaStore.EXTRA_OUTPUT));

            Log.d(CLASS_NAME, "this.assessmentDetailList is null? " + (this.assessmentDetailList == null));
            Log.d(CLASS_NAME,"saved photo: " + this.assessmentDetailList.getAssessmentDetailPhotoPath());
            if (this.assessmentDetailList !=null)
            {
                saveAssessmentDetails();
                ImageProcessor loadBitmap = new ImageProcessor(App.getContext());
                loadBitmap.initFileName(this.assessmentDetailList.getAssessmentDetailPhotoPath());

                this.assessmentPhoto.setImageBitmap(loadBitmap.getImageBitmap());
            }

        }

    }

    /**
     * onBackPressed - makes sure the Activity gets saved before it's closed.
     */
    @Override
    public void onBackPressed(){
        updateAssessmentDetails();
        if (saveAssessmentDetails() > 0){
            commonFunctions.makeToast("Successfully saved changes. ");
        }
        else
        {
            commonFunctions.makeToast("Unable to save changes. ");
        }

        finish();
    }
}
