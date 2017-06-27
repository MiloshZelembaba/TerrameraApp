package com.example.miloshzelembaba.terramera;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by miloshzelembaba on 6/26/17.
 */

public class ActionableCard extends ArrayItem{


    private String header;
    private String title;
    private Activity context;
    public int colour;
    private InstructionSet set;
    public boolean completed = false;


    public ActionableCard(String h, String t, Activity c){
        header = h;
        title = t;
        context = c;
    }

    public InstructionSet getInstructionSet(){
        return set;
    }

    public void setColour(int colour){
        this.colour = colour;
    }

    public void changeLayoutColour(MainActivity activity){
        activity.findViewById(R.id.toolbar).setBackgroundColor(activity.getResources().getColor(colour));
        activity.findViewById(R.id.content_main).setBackgroundColor(activity.getResources().getColor(colour));
    }

    public String getHeader(){
        return header;
    }

    public String getTitle(){
        return title;
    }

    public void setInstructions(InstructionSet s){
        set = s;
    }

    public ArrayList<Instruction> getInstructions(){
        return set.getInstructions();
    }

    public void finish(){
        completed = true;
    }
}
