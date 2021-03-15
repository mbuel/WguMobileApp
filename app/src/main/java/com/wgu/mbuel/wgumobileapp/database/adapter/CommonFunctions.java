package com.wgu.mbuel.wgumobileapp.database.adapter;

/**
 * Created by mbuel on 4/30/2017.
 */

import android.util.Log;
import android.widget.Toast;

import com.wgu.mbuel.wgumobileapp.app.App;

/**Goal of this class and it's methods are to safely convert a string into an integer
 * Added method to make calling a toast easier
 *
 */
public class CommonFunctions {
    private final String CLASS_NAME = this.getClass().getName();

    private int integer;
    private String string;

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean tryParseInt(String string)
    {
        setString(string);
        try{
            this.integer = Integer.parseInt(this.string);
            return true;
        }
        catch(NumberFormatException e)
        {
            Log.d(CLASS_NAME,"Problem parsing String to integer: " + string + "\n" + e.getMessage());
            this.integer = 0;
            return false;

        }
    }
    public void makeToast(String toast)
    {
        Toast.makeText(App.getContext(),toast,Toast.LENGTH_LONG).show();
    }
}
