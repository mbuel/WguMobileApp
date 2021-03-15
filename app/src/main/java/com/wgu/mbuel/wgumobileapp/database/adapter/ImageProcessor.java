package com.wgu.mbuel.wgumobileapp.database.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;

import com.wgu.mbuel.wgumobileapp.app.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by mbuel on 4/17/2017.
 * Code taken from example on stackOverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
 */

public class ImageProcessor {
    private final String CLASS_NAME = this.getClass().getName();
    private static final String IMAGE_PATH = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/";
    private String filename ="";

    private Context context;

    /**
     * ImageProcessor(Context) instantiation to set up variables and context
     *
     * @param context context for this class
     */
    public ImageProcessor(Context context)
    {
        this.context = context;
        File newDir = new File(IMAGE_PATH);
        newDir.mkdirs();
    }

    /**
     * sets up the filename
     * @param imagePath
     */
    public void initFileName(String imagePath)
    {
        this.filename = imagePath;
    }

    /**
     * deletePhoto - deletes the photograph
     * @param photoToDelete
     * @return true or false on whether photo was deleted
     */
    public boolean deleteFile(String photoToDelete)
    {
        photoToDelete = photoToDelete.substring(photoToDelete.indexOf(":")+2);
        File fileToDelete = new File(photoToDelete);
        Log.d(CLASS_NAME,fileToDelete.toString());

        try
        {
            return fileToDelete.getCanonicalFile().delete();
        }
        catch(Exception e)
        {
            Log.d(CLASS_NAME,"FAILED TO DELETE: " + e.getMessage());
            return false;
        }

    }

    /**
     * getImageBitmap - requires setting local filename, then calls method to return Bitmap
     * @return Bitmap object to return to calling Activity
     * @throws NullPointerException
     */
    public Bitmap getImageBitmap() throws NullPointerException{
        if (this.filename !=null && this.IMAGE_PATH !=null)
        {
            return getImageBitmap(this.context,this.filename);
        }
        else
        {
            throw new NullPointerException("Cannot use this method without first setting image name and file path.");
        }
    }

    /**
     * getImageBitmap - returns Bitmap resource from context and filename
     * @param context - passed in context
     * @param filename - filename to stream to a bitmap object
     * @return Bitmap object to activity
     */
    public Bitmap getImageBitmap(Context context,String filename){

        try{
            InputStream fis = (InputStream) new URL(filename).getContent();
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
            Log.d(CLASS_NAME,"problem returning Bitmap. " + e.getMessage());
        }
        return null;
    }
}
