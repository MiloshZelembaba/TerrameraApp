package com.example.miloshzelembaba.terramera;

import android.app.Notification;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<ArrayItem> actionableCards = new ArrayList<>();
    private ActionableCardAdapter actionableCardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* creating the adapter and adding the cards */
        ActionableCard detectBedBugs = new ActionableCard(ActionableCardStrings.DETECTION_HEADER,
                ActionableCardStrings.DECETION_TITLE, this);
        addDetectionInstructions(detectBedBugs);
        ActionableCard treatBedBugs = new ActionableCard(ActionableCardStrings.TREATMENT_HEADER,
                ActionableCardStrings.TREATMENT_TITLE, this);
        addTreatmentInstructions(treatBedBugs);
        ActionableCard preventBedBugs = new ActionableCard(ActionableCardStrings.PREVENTION_HEADER,
                ActionableCardStrings.PREVENTION_TITLE, this);
        addPreventionInstructions(preventBedBugs);
        actionableCards.add(new Blurb());
        actionableCards.add(detectBedBugs);
        actionableCards.add(treatBedBugs);
        actionableCards.add(preventBedBugs);

        actionableCardAdapter = new ActionableCardAdapter(this, R.layout.actionable_card, actionableCards);

        ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setAdapter(actionableCardAdapter);



        //TODO: MAKE THIS SUPPORT BUTTON
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setUpLesson(String type, ActionableCard card){
        card.changeLayoutColour(this);
        showLesson(card);
    }

    public void showLesson(ActionableCard card){
        Blurb blurb = new Blurb();
        blurb.setTitle(card.getTitle());
        ArrayList<Instruction> instructions = card.getInstructions();
        ArrayList<ArrayItem> items = new ArrayList<>();
        items.add(blurb);
        for (Instruction i: instructions){
            items.add(i);
        }

        LessonAdapter lessonAdapter = new LessonAdapter(this, R.layout.minimal_lesson, items);

        ListView listView = (ListView) findViewById(R.id.actionable_card_list);
        listView.setAdapter(lessonAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDetectionInstructions(ActionableCard card){
        InstructionSet set = new InstructionSet();
        Instruction step1 = new Instruction("Turn on your flashlight", "");
        Instruction step2 = new Instruction("Go under your bed", "");
        Instruction step3 = new Instruction("Freak out", "");
        set.add(step1);
        set.add(step2);
        set.add(step3);

        card.setInstructions(set);
    }

    private void addPreventionInstructions(ActionableCard card){
        InstructionSet set = new InstructionSet();
        Instruction step1 = new Instruction("Turn on your flashlight", "");
        Instruction step2 = new Instruction("Go under your bed", "");
        Instruction step3 = new Instruction("Freak out", "");
        set.add(step1);
        set.add(step2);
        set.add(step3);

        card.setInstructions(set);
    }

    private void addTreatmentInstructions(ActionableCard card){
        InstructionSet set = new InstructionSet();
        Instruction step1 = new Instruction("Turn on your flashlight", "");
        Instruction step2 = new Instruction("Go under your bed", "");
        Instruction step3 = new Instruction("Freak out", "");
        set.add(step1);
        set.add(step2);
        set.add(step3);

        card.setInstructions(set);
    }
}
