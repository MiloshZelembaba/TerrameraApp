package com.example.miloshzelembaba.terramera;

import android.app.Activity;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class ActionableCard {


    private String header;
    private String title;
    private Activity context;


    public ActionableCard(String h, String t, Activity c){
        header = h;
        title = t;
        context = c;
    }

    public String getHeader(){
        return header;
    }

    public String getTitle(){
        return title;
    }
}
